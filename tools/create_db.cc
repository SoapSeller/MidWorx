#include <fstream>
#include <iostream>
#include <map>
#include <set>
#include <string>
#include <vector>
#include <algorithm>

/* This program generates the index file for words.
 * Format of the index file:
 * <6 letter words count>[<location of index><num of words>, ...][[<location of word>, ...], ...]
 *    6 letter words count: Kind of obvious.
 *    location of index: Location of the current chosen query in this file. One
 *        should seek for (1 + <6 letter words count> * 2 + <location of index>).
 *    num of words: Number of words for the current query.
 *    location of word: The location of the current word in the words file. Just
 *        seek there.
 */

using namespace std;

typedef map<pair<string, map<char, size_t> >, uint32_t> AllWords;

void rtrim(string& s) {
  s.erase(std::find_if(s.rbegin(), s.rend(), std::not1(std::ptr_fun<int, int>(std::isspace))).base(), s.end());
}

map<char, size_t> break_word(string word) {
  map<char, size_t> broken;
  for (size_t i = 0; i < word.size(); ++i) {
    broken[word[i]]++;
  }
  return broken;
}

bool not_alpha(char c) {
  return !isalpha(c);
}

pair<AllWords, set<map<char, size_t> > > read_words(string fname) {
  AllWords all_words;
  set<map<char, size_t> > six_letter_words;
  ifstream file(fname.c_str());
  string word;
  uint32_t seek;
  while (!file.eof()) {
    seek = file.tellg();
    getline(file, word);
    rtrim(word);
    std::transform(word.begin(), word.end(), word.begin(), ::tolower);
    map<char, size_t> broken_word = break_word(word);
    if ((word.size() < 3) || (word.size() > 6) ||
        (find_if(word.begin(), word.end(), not_alpha) != word.end())) {
      continue;
    }
    all_words[make_pair(word, broken_word)] = seek;
    if (word.size() == 6) {
      sort(word.begin(), word.end());
      six_letter_words.insert(broken_word);
    }
  }
  file.close();
  return make_pair(all_words, six_letter_words);
}

bool check_word_in_word(map<char, size_t> short_word, map<char, size_t> long_word) {
  for (map<char, size_t>::const_iterator short_iter = short_word.begin(); short_iter != short_word.end(); ++short_iter) {
    if (short_iter->second > long_word[short_iter->first]) {
      return false;
    }
  }
  return true;
}

void create_index(string fname, const set<map<char, size_t> >& six_letter_words, const AllWords& all_words) {
  ofstream file(fname.c_str());
  uint32_t num_six_letter_words = six_letter_words.size();
  file.write(reinterpret_cast<char*>(&num_six_letter_words), sizeof(uint32_t));

  vector<pair<uint32_t, uint32_t> > index_index;
  vector<uint32_t> word_index;

  uint32_t idx = 0;
  for (set<map<char, size_t> >::const_iterator iter = six_letter_words.begin(); iter != six_letter_words.end(); ++iter) {
    uint32_t word_count = 0;
    for (AllWords::const_iterator words_iter = all_words.begin(); words_iter != all_words.end(); ++words_iter) {
      if (check_word_in_word(words_iter->first.second, *iter)) {
        word_count++;
        word_index.push_back(words_iter->second);
      }
    }
    index_index.push_back(make_pair(idx, word_count));
    idx += word_count;
  }

  file.write(reinterpret_cast<char*>(index_index.data()), index_index.size() * sizeof(uint32_t) * 2);
  file.write(reinterpret_cast<char*>(word_index.data()), word_index.size() * sizeof(uint32_t));

  file.close();
}

int main(int argc, char** argv) {
  if (argc != 3) {
    cout << "Usage: " << argv[0] << " input_file output_file" << endl;
    return 1;
  }
  pair<AllWords, set<map<char, size_t> > > pair_words = read_words(argv[1]);
  AllWords& all_words = pair_words.first;
  set<map<char, size_t> >& six_letter_words = pair_words.second;
  create_index(argv[2], six_letter_words, all_words);
  return 0;
}
