from cond_simulator import simulate

import torch
import torch.nn as nn
import torch.functional as F

import pyro
import pyro.distributions as dist
import pyro.infer
import pyro.optim

import numpy as np
import scipy.stats
import matplotlib.pyplot as plt

import os
import traceback
from time import time
from statistics import mean, median, variance

num_samples=100
VISUAL=True

def model():
    result=simulate()
    pyro.sample("cond",dist.Normal(torch.tensor(10.0),torch.tensor(1.00)),obs=torch.tensor(result))
    

is_=pyro.infer.Importance(model, num_samples=num_samples)
t0=time()
is_posterior = is_.run()
t1=time()
is_marginal = pyro.infer.EmpiricalMarginal(is_posterior, "param_0")
if VISUAL:
    print("Effective sample size = ",is_.get_ESS())
    #print(is_marginal())
    is_samples = torch.stack([torch.abs(is_marginal()) for _ in range(10000)])
    print([is_marginal() for _ in range(10)])
    fig, a = plt.subplots()
    print("Sample size = ",num_samples,", estimated mean = ",is_marginal.mean, ", calculation time = ",t1-t0)
    a.set_title( "Empirical marginal (ESS:"+str(round(is_.get_ESS().item(),2))+", avg:"+str(round(is_marginal.mean.item(),2))+", stddev:"+str(round(is_marginal.variance.sqrt().item(),2))+")" )
    a.hist(is_samples.numpy(), color='b',bins=int(50), density=1,label="Importance Sampling")
    plt.ylabel("estimated posterior density")
    plt.xlabel("estimated uC failure time")
    plt.show()
