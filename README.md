# FP-Growth Algorithm

## Execution

To compile run:

```bash
javac main.java
javac fpgrowth.java
```

To execute run:

```bash
java main <filepath> <support level>
```

### Example

```bash
java main ./Datasets/data.txt 2
```

Will run the FP-Growth algorithm on the database file found at ./Datasets/data.txt, with a minimum support level of 2. An ouput file (MiningResult.txt) will be created in the root directory, containing association rules for the frequent itemsets.

### Example file output

|FPs| = 9</br>
[2] : 4</br>
[2, 5] : 4</br>
[2, 3, 5] : 2</br>
[2, 3] : 2</br>
[5] : 4</br>
[3, 5] : 2</br>
[3] : 3</br>
[1, 3] : 2</br>
[1] : 2
