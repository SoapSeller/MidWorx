def to_num(x):
	return ord(x[0]) + ord(x[1])*256 + ord(x[2])*256*256 + ord(x[3])*256*256*256

six_to_sub_words = {}
offset_to_numwords = {}
six_letter_word_count = None

bytes_to_word = {}
word_to_byte = {}

english_words = open("../MidWorx/src/main/assets/words").read().split("\r\n")

def bytes_from_file(filename, chunksize=8192):
    with open(filename, "rb") as f:
        while True:
            chunk = f.read(chunksize)
            if chunk:
		yield chunk
            else:
                break


currentByteKey = None
byteCount = 0

bytes_to_word[0] = []
for b in bytes_from_file("../MidWorx/src/main/assets/words",1):
	if (b == "\r"):
		if currentByteKey != None:
			word_to_byte[ "".join(bytes_to_word[currentByteKey]) ] = currentByteKey
		currentByteKey = None
	else:
		if (b != "\r" and b != "\n"):
			if currentByteKey == None:
				currentByteKey = byteCount
				bytes_to_word[currentByteKey] = []
			bytes_to_word[currentByteKey].append(b)

	byteCount += 1


c = 0
vc = 0
current_vc = 0
current_key = None
key = None
tmp_buffer = []
#for b in bytes_from_file("../MidWorx/src/main/assets/words.idx",4):
for b in bytes_from_file("bla.idx",4):
	num_val = to_num(b)
	if not six_letter_word_count:
		six_letter_word_count = num_val
	else:
		# initial mapping of word sectinos
		if c < six_letter_word_count:
			if key is None:
				key = num_val
			else:
				offset_to_numwords[key] = num_val
				key = None
				c += 1
		# actual word mapping action
		else:
			if offset_to_numwords.has_key(vc):
				current_key = num_val
				if vc != 0 and current_vc + offset_to_numwords[current_vc] == vc:
					for widx in tmp_buffer:
						if len(bytes_to_word[widx]) == 6:
							six_to_sub_words[widx] = tmp_buffer
				tmp_buffer = []
				current_vc = vc
			tmp_buffer.append(num_val)
			vc += 1 



# def find_word(word):
	# wordIndex ]

bad_word = "fibred"
# 52528
# loca = word_to_byte["abandon"]
# print bytes_to_word[loca]

# print sorted(bytes_to_word.keys())
# for num in sorted(six_to_sub_words.keys()):
# 	print bytes_to_word[num]
# print six_to_sub_words
# print bytes_to_word[2466]
bad_loc = word_to_byte[bad_word]
# print bad_loc
# print bytes_to_word
for num in six_to_sub_words[bad_loc]:
	print bytes_to_word[num]
# 0, 8, 19, 31, 41, 45, 52, 59, 67, 77, 84, 93, 103, 114, 123, 134, 145
# print max(six_to_sub_words.keys())
# print find_word("key")
# print six_to_sub_words