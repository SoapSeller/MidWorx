import urllib2
import json



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

    def the_word_loop(self):
        wordfile = open("../MidWorx/src/main/assets/words")
        for word in wordfile:
            word = word.strip()
            print '==', word, '=='
            try:
                d = self.lookup.get_def(word)
                if d.is_primary():
                    print 'primary'
                    print >>self.out_primary, word
                else:
                    print 'web'
                    print >>self.out_webdef, word
            except:
                print 'failed'
                print >>self.out_failed, word



if __name__ == '__main__':
    Category().the_word_loop()
