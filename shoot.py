#!/usr/bin/env python
import os
from optparse import OptionParser
from beanstalkc import Connection

HOST = os.environ.get('HOST', 'localhost')
PORT = os.environ.get('PORT', 11300)

def main():
    parser = OptionParser()
    parser.add_option("--i", default=50)
    parser.add_option("--n", default=10)
    parser.add_option("--tube", default="default")
    
    options, args = parser.parse_args()

    conn = Connection(HOST, PORT)
    conn.use(options.tube)

    for i in range(int(options.n)):
        conn.put(options.i) 

if __name__ == '__main__':
    main()
