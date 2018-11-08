#otp_client.py
#author: Jon Dycaico

import hashlib as hl
import matplotlib.pyplot as plt
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
    print('(any key to proceed, q to quit)\n')
    response = input('generate key? ')
    while response.lower() != 'q':
        generate_otp()
        print('    OTP ' + str(len(keys)) + ': ' + str(keys[-1]))
        response = input('generate key? ')


def bulkmode(quantity):
    for i in range(int(quantity)):
        generate_otp()


def graph(quantities, collisions):
    plt.figure(0)
    plt.title('OTP Collisions')
    plt.xlabel('qty of OTPs generated')
    plt.ylabel('# of collisions')
    plt.plot(quantities, collisions)
    plt.show()
    plt.savefig('graph.png')


def collision():
    quantities = [1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000]
    collisions = []
    total_processed = 0

    for value in quantities:
        bulkmode(value - total_processed)
        total_processed += value
        unique_OTPs = []
        for item in keys:
            if item not in unique_OTPs:
                unique_OTPs.append(item)
        collisions.append(total_processed - len(unique_OTPs))

    graph(quantities, collisions)


def write_keys():
    file = open('output.txt', 'w')
    for item in keys:
        file.write(str(item) + '\n')
    file.close()

def print_hashes():
    for element in hashes:
        print('    ' + element)


#'main':
print('--- OTP client ---')
modeset = input('Input \'a\' for user mode; \'b\' for bulk mode, or \'c\' for collision mode    ')
if modeset.lower() == 'a':
    usermode()
elif modeset.lower() == 'b':
    print('BULK MODE')
    qty = int(input('Enter the number of OTPs you want to generate:   '))
    print(str(qty) + '\n')
    bulkmode(qty)
    write_keys()
elif modeset.lower() == 'c':
    collision()
else:
    print('invalid mode selected')
    sys.exit(-1)
