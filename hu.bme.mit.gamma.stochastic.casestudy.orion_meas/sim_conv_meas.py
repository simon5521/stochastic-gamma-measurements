from simulator import simulate
from jpype import *

from statistics import mean, median, variance
import csv

sample_nums=[10,20,50,100,200,500]
repeat_num=10
meas=[]


try:
    print("run importance sampling")
    for num_samples in sample_nums:
        results=[]
        for r in range(repeat_num):
            data_buff=[]
            print("run analysis with sample_num[",r,"]: ",num_samples)
            for i in range(num_samples):
                data_buff.append(simulate())
            results.append(mean(data_buff))
        meas.append(results)
        print(num_samples,"; ",mean(results),"; ",median(results),"; ",variance(results),"; ",results)
except Exception as err:
    print("Exception occured during testing the simulation: ")
    print(err)
    traceback.print_exc()
except java.lang.RuntimeException as ex:
    print("Caught runtime exception : ", str(ex))
    print(ex.stacktrace())
except jpype.JException as ex:
    print("Caught base exception : ", str(ex))
    print(ex.stacktrace())
except Exception as ex:
    print("Caught python exception :", str(ex))
except Exception as err:
    print("Exception occured during testing the simulation: ")
    print(err)
    traceback.print_exc()
finally:
    shutdownJVM()
    
print("Save measurements to csv file...")
with open('./orion_sim_conv_meas.csv', 'w', encoding='UTF8') as f:
    writer = csv.writer(f)
    for row in meas:
        writer.writerow(row)


