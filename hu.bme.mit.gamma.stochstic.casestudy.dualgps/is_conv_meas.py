from cond_simulator2 import simulate
from jpype import *

import csv
import os
import traceback
from time import time
from statistics import mean, median, variance

import pyro
import pyro.distributions as dist
import pyro.infer
import pyro.optim
import torch

sample_nums=[10,20,50,100,200,500]
repeat_num=10
meas=[]

def model():
    result=simulate()
    pyro.sample("cond",dist.Normal(torch.tensor(0.4),torch.tensor(0.05)),obs=torch.tensor(result))
    


try:
    print("run importance sampling")
    for num_samples in sample_nums:
        results=[]
        for r in range(repeat_num):
            data_buff=[]
            is_=pyro.infer.Importance(model, num_samples=num_samples)
            t0=time()
            is_posterior = is_.run()
            t1=time()
            is_marginal = pyro.infer.EmpiricalMarginal(is_posterior, "param_0")
            results.append(is_marginal.mean.item())
            print("run analysis with sample_num[",r,"]: ",num_samples," time = ",t1-t0)
        meas.append(results)
        print(num_samples,"; ",mean(results),"; ",median(results),"; ",variance(results),"; ",results)
    print("Save measurements to csv file...")
    with open('./dualgps_is_conv_meas.csv', 'w', encoding='UTF8') as f:
        writer = csv.writer(f)
        for row in meas:
            writer.writerow(row)
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
    



