'''
Test configuration
'''

#DSTAT CONFIG
'''
    Possible values for dstat_mode
    1) None: nothing should be done
    2) "file": MBPeT will prompt at the end of the experiment for dstart log file
    3) "ssh": Tester must provide host IP address, username and password to ssh into the server.
              MBPeT will automatically run the Dstat and retrieve the log file
'''
dstat_mode =  None   #If it is False, then host, username and password's values will be ignored

#Uncomment following lines if dstat_mode is "ssh"
#host = "127.0.0.1" 
#username = 'mbpet_user'
#password = 'mbpet_user'


#remeber to change the values below to your local destinations
user_types = "user_types.gv"
models_folder = "models/"
test_report_folder = "test_reports/"


ip = "google.com"    # ip-address to test connection with server

#TEST CONFIG
#===================== Length of the test time ==========================
'''
Desribed in seconds how long you want to test.
E.g. test_duration =  3600 (60 sec * 60 min) runs the test for 1 hour
'''
test_duration = 3600
#=========================== Ramp Coefficient =================================
'''
The ramp period is defined as a list of tuples. The first integer in a
tuple describes the time duration in seconds since the experiment
started and the second integer states the target number of concurrent
users at that moment.
'''
ramp_list = [(0, 0), (250, 400)]
#ramp_list = [(0, 0), (500, 300), (720, 300), (870, 150), (1200, 150)]
#ramp_list = [(0,0), (70,70),(150,70),(250,30),(350,30),(400,100)]  
#ramp_list = [(0,0), (60,30)]
#ramp_list = [(0,0),(550,505),(600,505),(700,200),(800,200),(1200,800)]

#=========================== Target KPIs =================================
'''
For each action found in the user models, a target average and max response time should be defined in a dictionary called TargetResponseTime.
E.g. TargetResponseTime = {'action1()':{'average':0.5,'max':3.5}, 'action2()': {'average':0.04,'max': 10.4}}
'''
TargetResponseTime = {'search_on_google(car)': {'average':0.5,'max':1.0},'search_on_bing(ferrari)': {'average':0.5,'max':1.0},'search_on_yahoo(boeing)': {'average':0.5,'max':1.0},'exit()': {'average':0.0,'max':0.0}}

#=========================== Monitoring interval =================================
'''
Specifies (in seconds) the interval of how often to check the load of the client machine
E.g. interval = 1 would log the client load (Disk, network, memory and cpu) every second
'''
interval = 3
#=========================== Think Time =================================
'''
Describes how long (in seconds) users should think between actions following a normal distribution
E.g. mean_user_think_time = 3
E.g. standard_deviation = 1

     The user think time now follows a normal distribution with mean value of 3 and standard deviation of 1
'''
mean_user_think_time = 3
standard_deviation = 1.0
