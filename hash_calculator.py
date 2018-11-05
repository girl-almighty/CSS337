import hashlib as hl

hashes = []

seed = '808670FF00FF08812'
print('seed: ' + str(type(seed)))

enc_seed = seed.encode()
print('enc_seed: ' + str(type(enc_seed)))


first_hash_before_hexdigest = hl.sha256(enc_seed)
print('\nfirst_hash (before hexdigest):')
print(first_hash_before_hexdigest)
print('type: ' + str(type(first_hash_before_hexdigest)))
first_hash = first_hash_before_hexdigest.hexdigest()
print('after hexdigest: ')
print(first_hash)
print('type: ' + str(type(first_hash)))
hashes.append(first_hash)

second_hash = hl.sha256(hashes[-1].encode()).hexdigest()
print('\nsecond_hash')
print(second_hash)
print('type: ' + str(type(second_hash)))
hashes.append(second_hash)

third_hash = hl.sha256(hashes[-1].encode()).hexdigest()
print('\nsecond_hash')
print(second_hash)
print('type: ' + str(type(second_hash)))
print('seed of third_hash: ')
print(hashes[-1])
