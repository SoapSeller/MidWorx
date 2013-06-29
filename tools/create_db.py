#!/usr/bin/python

import sqlite3
import os

def create_tables(conn):
  c = conn.cursor()
  c.execute('''
    CREATE TABLE sixLetterWords(
      word text not null primary key)
  ''')

  c.execute('''
    CREATE TABLE allWords(
      key text,
      word text,
      FOREIGN KEY(key) REFERENCES sixLetterWords(word))
  ''')

  conn.commit()

def add_six_letter_word(word, c):
  c.execute('INSERT INTO sixLetterWords VALUES ("%s")' % word)

def add_word(word, six_letter_word, c):
  c.execute('INSERT INTO allWords VALUES ("%s", "%s")' % (six_letter_word, word))

def check_in_word(short_dict, long_dict):
  for k, v in short_dict.iteritems():
    if k not in long_dict or v > long_dict[k]:
      return False
  return True

def main():
  fname = "../MidWorx/src/main/assets/words.db"
  try:
    os.remove(fname)
  except:
    pass

  conn = sqlite3.connect(fname)
  create_tables(conn)
  l = [line.strip().lower() for line in file("words") if 6 >= len(line.strip()) >= 3]
  six_letter_words = list()
  c = conn.cursor()
  for x in l:
    if len(x) != 6:
      continue
    add_six_letter_word(x, c)
    d = {}
    for letter in x:
      if letter not in d:
        d[letter] = 0
      d[letter] += 1
    six_letter_words.append((x, d))
  conn.commit()
  c = conn.cursor()
  for x in l:
    d = {}
    for letter in x:
      if letter not in d:
        d[letter] = 0
      d[letter] += 1
    for word, breakdown in six_letter_words:
      if check_in_word(d, breakdown):
        add_word(x, word, c)
  conn.commit()
  conn.close()


if __name__ == "__main__":
  main()
