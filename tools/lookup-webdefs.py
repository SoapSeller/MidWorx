#!/usr/bin/python

import urllib2
import json
import multiprocessing.pool
import threading


class Lookup(object):
    URL_DEFINE = "http://www.google.com/dictionary/json?callback=x&q=%(word)s&sl=en&tl=en"

    class Definition(dict):
        def is_primary(self):
            return 'primaries' in self

    def get_def(self, word):
        x = urllib2.urlopen(self.URL_DEFINE % {'word': word}).read()
        x = x[2:-10].replace(r"\x", r"\u00")
        return self.Definition(json.loads(x))
    

class Category(object):

    def __init__(self):
        self.lookup = Lookup()
        self.out_primary, self.out_webdef, self.out_failed = (
            open('words-primary.txt', 'w'),
            open('words-webdef.txt', 'w'),
            open('words-failed.txt', 'w'))
        self.lock = threading.Lock()
        self.nthreads = 64
        self.word_filename = "../MidWorx/src/main/assets/words"

    def the_word_loop(self):
        wordfile = open(self.word_filename)
        pool = multiprocessing.pool.ThreadPool(self.nthreads)
        def process_word(word):
            word = word.strip()
            print '==', word, '=='
            try:
                d = self.lookup.get_def(word)
                with self.lock:
                  if d.is_primary():
                      print 'primary'
                      print >>self.out_primary, word
                  else:
                      print 'web'
                      print >>self.out_webdef, word
            except:
              with self.lock:
                print 'failed'
                print >>self.out_failed, word
        pool.map(process_word, wordfile)



if __name__ == '__main__':
    import argparse
    a = argparse.ArgumentParser(description="Sort words into categories")
    a.add_argument('--wordfile', default="../MidWorx/src/main/assets/words",
            help="text file containing words (one per line)")
    a.add_argument('--jobs', type=int, default=1, 
            help="number of worker threads")
    a = a.parse_args()

    c = Category()
    c.nthreads = a.jobs
    c.word_filename = a.wordfile
    
    c.the_word_loop()
