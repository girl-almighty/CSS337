import hashlib as hl

seed = '808670FF00FF08812'

bin_seed = seed.encode()
print(bin_seed)

first_hash = hl.sha256(bin_seed)
print(first_hash)

hash_digest = first_hash.hexdigest()
print(hash_digest)

int_of_hash_digest = int(hash_digest, 16)
print(int_of_hash_digest)
