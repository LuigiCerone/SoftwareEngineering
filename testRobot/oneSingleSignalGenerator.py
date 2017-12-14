import sys, json, random, string
from datetime import datetime
from random import randint
import requests
from multiprocessing import Pool
import time


start_time = time.time()
file = open("one_single_signal_output.txt", "w")


def getRandom(num):
	return ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(num))

def get_fake_robot_data():
    data = {}
    data.update({
        'robot': robot,
        'cluster': cluster,
        'zone': zone,
        'signal': 1 ,
        'value': randint(0, 1),
        'timestamp': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
        })
    return data

def send_request():
	try:
		headers = {'Content-type': 'application/json'}
		data = json.dumps(get_fake_robot_data())
		r = requests.post(url, data=data, headers=headers)
		print(data)
		file.write(data)
	except ConnectionError as e:    # This is the correct syntax
		print ("Server is not responding!")
		exit()

url = 'http://127.0.0.1:9000/robots'

robot = getRandom(3)
cluster = getRandom(3)
zone = getRandom(3)

send_request()
time.sleep(30)
send_request()
# for i in range(10):
# 	send_request()
# 	time.sleep(30)


file.close()
