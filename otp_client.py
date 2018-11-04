#otp_client.py
#author: Jon Dycaico

import hashlib as hl

hashes = []
keys = []


seed = '808670FF00FF08812'

first_hash = hl.sha256(seed.encode()).hexdigest()
hashes.append(first_hash)

first_key = str(int(first_hash, 16))[:6]
keys.append(first_key)

#print(hashes)
#print(keys)

def generate_otp():
    new_hash = hl.sha256(hashes[-1].encode()).hexdigest()
    new_key = str(int(new_hash, 16))[:6]
    hashes.append(new_hash)
    keys.append(new_key)
    print('\033[1m' + new_key + '\033[0m')

print('--- OTP client ---\n')
print('first key:')
print('\033[1m' + keys[0] + '\033[0m')
response = input('generate key? (y/n)    ')
while response.lower() != 'n':
    generate_otp()
    response = input('generate key? (y/n)    ')

print('Hashes:')
for element in hashes:
    print('    ' + element)

print('Keys:')
print(str(keys) + '\n')

#print(hashes)
#print(keys)
