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
import pyro.poutine as poutine
from pyro.infer import MCMC, NUTS

import os
import traceback
from time import time
from statistics import mean, median, variance
warmup_steps=0
num_samples=5
num_chains=1
VISUAL=True

def model():
    result=simulate()
    pyro.sample("cond",dist.Normal(torch.tensor(0.4),torch.tensor(0.05)),obs=torch.tensor(result))
    
nuts_kernel = NUTS(model)
mcmc = MCMC(
        nuts_kernel,
        num_samples=num_samples,
        warmup_steps=warmup_steps,
        num_chains=num_chains,
    )
t0=time()
posterior=mcmc.run()
t1=time()
#mcmc.summary(prob=0.5)
marginal = pyro.infer.EmpiricalMarginal(posterior, "param_0")
if VISUAL:
    #print(is_marginal())
    is_samples = torch.stack([torch.abs(marginal()) for _ in range(10000)])
    print([is_marginal() for _ in range(10)])
    fig, a = plt.subplots()
    print("Sample size = ",num_samples,", estimated mean = ",marginal.mean, ", calculation time = ",t1-t0)
    a.set_title( "Empirical marginal (ESS:"+str(round(is_.get_ESS().item(),2))+", avg:"+str(round(marginal.mean.item(),2))+", stddev:"+str(round(marginal.variance.sqrt().item(),2))+")" )
    a.hist(is_samples.numpy(), color='b',bins=int(50), density=1,label="MCMC NUTS")
    plt.ylabel("estimated posterior density")
    plt.xlabel("estimated uC failure time")
    plt.show()
