'''
Note: Instead of importing a whole module, ALWAYS import required function or classes
Good Example !!!
From sys import exit
from thread import sleep

Bad Example !!!
Import sys
from datetime import *

Importing a whole module will have negative memory impact when running large numbers of concurrent users.
'''

from datetime import datetime as datetime
from datetime import timedelta
from imp import load_source 
from os.path import join as path_join
from urllib2 import urlopen


class Generic_Adapter():

    def __init__(self,project_path,number_of_users,remote_resource_db = {}):

        self.project_path = project_path
	self.number_of_users = number_of_users
  	self.remote_resource_db = remote_resource_db
        self.settings = load_source('settings',path_join(self.project_path,'settings.py'))
        
 
    def getResourceDB(self):
        # Master calls this function immediatelly after __init__
        print "Initializing database..."
        '''
        In here one can initialize the test data needed for the test
        
        '''
        self.remote_resource_db['user1'] = 'password1'  # Example   
        return self.remote_resource_db

    def execute_action(self, username, user_id, action, parameters):
        '''
        This method is called by the Slave whenever an action is found in the test model.
        
        repeat =True should be returned the Slave if a request fails and the user needs to retry the same action in order to proceed
        '''
        repeat = False
                
        #try:
        start_time = datetime.now()
        #==================================================
        if action == "search_on_google":
           print "User: "+str(username) + " is searching google for \"" +str(parameters[0])+"\""
           request_url = "http://www.google.com/#q="+str(parameters[0])
           response = urlopen(request_url)
           stop_time = datetime.now()
           status = response.getcode()
      
        elif action == "search_on_bing":
           print "User: "+str(username) + " is searching bing for \"" +str(parameters[0])+"\""
           request_url = "http://www.bing.com/search?q="+str(parameters[0])
           response = urlopen(request_url)
           stop_time = datetime.now()
           status = response.getcode()
          
        elif action == "search_on_yahoo":
           print "User: "+str(username) + " is searching yahoo for \"" +str(parameters[0])+"\""
           request_url = "http://search.yahoo.com/search?p="+str(parameters[0])
           response = urlopen(request_url)
           stop_time = datetime.now()
           status = response.getcode()
           
        delta = stop_time - start_time
        print "User: "  + str(username)+ ", Action: " + str(action) + " DONE. DELTA: " + str(delta)
        return (delta, str(status), repeat)
        
       

    



