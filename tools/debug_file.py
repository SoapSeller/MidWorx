def to_num(x):
	return ord(x[0]) + ord(x[1])*256 + ord(x[2])*256*256 + ord(x[3])*256*256*256

six_to_sub_words = {}
offset_to_numwords = {}
six_letter_word_count = None
english_words = open("../MidWorx/src/main/assets/words").read().split("\r\n")

def bytes_from_file(filename, chunksize=8192):
    with open(filename, "rb") as f:
        while True:
            chunk = f.read(chunksize)
            if chunk:
		yield chunk
            else:
                break
def find_word(word):
	wordIndex = english_words.index(word)
	

c = 0
key = None
tmp_buffer = []
for b in bytes_from_file("../MidWorx/src/main/assets/words.idx",4):
	num_val = to_num(b)
	if not six_letter_word_count:
		six_letter_word_count = num_val
	else:
		if c < six_letter_word_count:
			if key is None:
				key = num_val
			else:
				six_to_sub_words[key] = num_val
				key = None
				c += 1
		else:
			loc_index = c - six_letter_word_count
			if six_to_sub_words.has_key(loc_index):
				if len(tmp_buffer):
					
				
				
			offset_to_numwords[c - six_letter_word_count] = num_val
			c += 2
	
print find_word("yuppie")
