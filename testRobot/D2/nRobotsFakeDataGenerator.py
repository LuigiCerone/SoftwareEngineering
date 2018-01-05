import sys, json, random, string, datetime
from random import randint
import requests
from requests.exceptions import ConnectionError
from datetime import datetime

file = open("three_robots_output.txt", "w")

def getRandom(num):
	return ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(num))

def get_fake_robot_data():
    data = {}
    data.update({
        'robot': id,
        'cluster': cluster,
        'zone': zone,
        'signal': randint(0, 8),
        'value': randint(0, 2),
        'timestamp': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
        })
    return data

def send_request():
    try:
    	headers = {'Content-type': 'application/json'}
    	data = json.dumps(get_fake_robot_data())
    	r = requests.post(url, data=data, headers=headers)
    	file.write(data)
    except ConnectionError as e:    # This is the correct syntax
        print ("Server is not responding!")
        exit()

url = 'http://127.0.0.1:9000/robots'
number = int(sys.argv[1])

for i in range(number):
    id = getRandom(5)
    cluster = getRandom(5)
    zone = getRandom(5)
    for i in range(7):
        send_request()
