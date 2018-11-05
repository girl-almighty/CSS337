#otp_client.py
#author: Jon Dycaico

import hashlib as hl

hashes = []
keys = []

seed = '808670FF00FF08812'

'''
first_hash = hl.sha256(seed.encode()).hexdigest()
hashes.append(first_hash)

first_key = str(int(first_hash, 16))[:6]
keys.append(first_key)
'''
#print(hashes)
#print(keys)

def generate_otp():
    if len(hashes) == 0:
        new_hash = hl.sha256(seed.encode()).hexdigest()
        new_key = str(int(new_hash, 16))[:6]
    else:
        new_hash = hl.sha256(hashes[-1].encode()).hexdigest()
        new_key = str(int(new_hash, 16))[:6]
    hashes.append(new_hash)
    keys.append(new_key)
    #print('\033[1m' + new_key + '\033[0m')


print('\033[93m' + '--- OTP client ---' + '\033[0m')
print('\033[95m' + '(y to proceed, n to quit)' + '\033[0m' + '\n')
#print('first key:')
#print('\033[1m' + keys[0] + '\033[0m')
response = input('generate key? ')
while response.lower() != 'n':
    generate_otp()
    print('    OTP ' + str(len(hashes)) + ': ' + '\033[1m' + str(keys[-1]) + '\033[0m')
    response = input('generate key? ')

print('\nHashes:')
for element in hashes:
    print('    ' + element)

print('Keys:')
print(str(keys) + '\n')

#print(hashes)
#print(keys)
