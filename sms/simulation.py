
from time import sleep
import os
import json
import time

count = 0
while True:
    data = {
        "date": int(time.time()),
        "high": 456.25,
        "open": 172.0,
        "close": 0.1,
        "low": 11.0,
        "volume": 745896.8
    }

    if os.path.isfile('/home/abm/MEGA/myprojects/ats/medium/data/responses/F@d@L.json'):
        continue
    with open('/home/abm/MEGA/myprojects/ats/medium/data/responses/F@d@L.json', 'w') as f:
        f.write(json.dumps(data))
        count+=1
    print(str(count) + '   data generated')
    sleep(1)