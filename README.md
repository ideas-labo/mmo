# MMO: Meta Multi-Objectivization for Software Configuration Tuning

This repository contains the data and code for the following published paper:

> Pengzhou Chen, Tao Chen, and Miqing Li. "MMO: Meta multi-objectivization for software configuration tuning." IEEE Transactions on Software Engineering, 2024.

## Introduction

Software configuration tuning is essential for optimizing a given performance objective (e.g., minimizing latency). Yet, due to the software’s intrinsically complex configuration landscape and expensive measurement, there has been a rather mild success, particularly in preventing the search from being trapped in local optima. To address this issue, in this work we take a different perspective. Instead of focusing on improving the optimizer, we work on the level of optimization model and propose a meta multi-objectivization (MMO) model that considers an auxiliary performance objective (e.g., throughput in addition to latency). What makes this model distinct is that we do not optimize the auxiliary performance objective, but rather use it to make similarly-performing while different configurations less comparable (i.e. Pareto nondominated to each other), thus preventing the search from being trapped in local optima. Importantly, by designing a new normalization method, we show how to effectively use the MMO model without worrying about its weight–the only yet highly sensitive parameter that can affect its effectiveness. Experiments on 22 cases from 11 real-world software systems/environments confirm that our MMO model with the new normalization performs better than its state-of-the-art single-objective counterparts on 82% cases while achieving up to 2:09× speedup. For 68% of the cases, the new normalization also enables the MMO model to outperform the instance when using it with the normalization from our prior FSE work under pre-tuned best weights, saving a great amount of resources which would be otherwise necessary to find a good weight. We also demonstrate that the MMO model with the new normalization can consolidate recent model-based tuning tools on 68% of the cases with up to 1:22× speedup in general.


## Data Result

The dataset of this work can be accessed via the Zenodo link [here](https://doi.org/10.5281/zenodo.5668778). In particular, the zip file contains all the raw data as reported in the paper; most of the structures are self-explained but we wish to highlight the following:

* The data under the folder `1.0-0.0` and `0.0-1.0` are for the single-objective optimizers. The former uses O1 as the target performance objective while the latter uses O2 as the target. The data under other folders named by the subject systems are for the MMO and PMO. The result under the weight folder `1.0` are for MMO while all other folders represent different weight values, containing the data for MMO-FSE.

* For those data of MMO, MMO-FSE, and PMO, the folder `0` and `1` denote using uses O1 and O2 as the target performance objective, respectively.

* In the lowest-level folder where the data is stored (i.e., the `sas` folder), `SolutionSet.rtf` contains the results over all repeated runs; `SolutionSetWithMeasurement.rtf` records the results over different numbers of measurements.

## Souce Code

The [`code`](https://github.com/ideas-labo/mmo/tree/main/code) folder contains all the information about the source code, as well as an executable jar file in the [`executable`](https://github.com/ideas-labo/mmo/tree/main/executable) folder .


## Running the Experiments

To run the experiments, one can download the `mmo-experiments.jar` from the aforementioned repository (under the [`executable`](https://github.com/ideas-labo/mmo/tree/main/executable) folder). Since the artifacts were written in Java, we assume that the JDK/JRE has already been installed. Next, one can run the code using `java -jar mmo-experiments.jar [subject] [runs]`, where `[subject]` and `[runs]` denote the subject software system and the number of repeated run (this is an integer and 50 is the default if it is not specified), respectively. The keyword for the systems/environments used in the paper are: 

* trimesh 
* x264
* storm-wc
* storm-rs
* dnn-sa
* dnn-adiac
* mariadb
* vp9
* mongodb
* lrzip
* llvm 

For example, running `java -jar mmo-experiments.jar trimesh` would execute experiments on the `trimesh` software for 50 repeated runs.

For each software system, the experiment consists of the runs for MMO, MMO-FSE with all weight values, PMO and the four state-of-the-art single-objective optimizers, as well as the FLASH and FLASH_MMO. All the outputs would be stored in the `results` folder at the same directory as the executable jar file.

All the measurement data of the subject configurable systems have been placed inside the `mmo-experiments.jar`.


