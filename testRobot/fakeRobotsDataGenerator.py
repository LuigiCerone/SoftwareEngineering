import sys, json, random, string, datetime
from random import randint
import requests
from multiprocessing import Pool
import time
from datetime import datetime


start_time = time.time()
file = open("all_robots_output.txt", "w")


def getRandom(num):
	return ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(num))

def get_fake_robot_data():
    data = {}
    data.update({
        'robot': getRandom(3),
        'cluster': getRandom(3),
        'zone': getRandom(3),
        'signal': randint(1, 7),
        'value': randint(0, 1),
        'timestamp': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
        })
    return data

def send_requests(times):
	for i in range(times):
		try:
			headers = {'Content-type': 'application/json'}
			data = json.dumps(get_fake_robot_data())
			r = requests.post(url, data=data, headers=headers)
			file.write(data)
			print(data)
		except ConnectionError as e:    # This is the correct syntax
			print ("Server is not responding!")
			exit()

url = 'http://127.0.0.1:9000/robots'
number_of_processes = 8
number = int(sys.argv[1])

if number < number_of_processes:
	print("Minium is " + str(number_of_processes))
	exit()

pool = Pool(processes=number_of_processes)
result = pool.map(send_requests, [int(number/number_of_processes)] * number_of_processes)
pool.close()
pool.join() # this blocks the program till function is run on all the items

print("--- %s seconds ---" % (time.time() - start_time))
file.close()
