#otp_client.py
#author: Jon Dycaico

import hashlib as hl
import sys

hashes = []
keys = []

seed = '808670FF00FF08812'

def generate_otp():
    if len(hashes) == 0:
        new_hash = hl.sha256(seed.encode()).hexdigest()
        new_key = str(int(new_hash, 16))[:6]
    else:
        new_hash = hl.sha256(hashes[-1].encode()).hexdigest()
        new_key = str(int(new_hash, 16))[:6]
    hashes.append(new_hash)
    keys.append(new_key)

def usermode():
    print('USER MODE')
    print('(y to proceed, n to quit)\n')
    response = input('generate key? ')
    while response.lower() != 'n':
        generate_otp()
        print('    OTP ' + str(len(hashes)) + ': ' + str(keys[-1]))
        response = input('generate key? ')

def bulkmode():
    print('BULK MODE')
    count = input('Enter the number of OTPs you want to generate:   ')
    for i in range(int(count)):
        generate_otp()

    file = open('output.txt', 'w')
    for item in keys:
        file.write(str(item) + '\n')
    file.close()


print('--- OTP client ---')
modeset = input('Input \'a\' for user mode or \'b\' for bulk mode  ')
if modeset.lower() == 'a':
    usermode()
elif modeset.lower() == 'b':
    bulkmode()
else:
    print('invalid mode selected')
    sys.exit(-1)

print('\nHashes:')
for element in hashes:
    print('    ' + element)
