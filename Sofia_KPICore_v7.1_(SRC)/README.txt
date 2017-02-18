Java KPI V7.1

Improved many features and performances of java KPI v6.x

Among changes :

- parsing optimization
- new class representing a SIB response
- new interface for subscriptions allowing different handlers for different subscriptions of the same KP
- improvement of GUI
- improvement of attached resources i.e. ontology loader and usage instruction class

Some advanced functionalities works only on OSGI SIB and not on V-SIB

Read and execute resources/KPIPrimitiveUage.java to understand basic features and see simple examples

resources/JenaBasedOntologyLoader is an untility to load ontology into SIB, to be run it requires Jena libraries on build path and configuration directly in code

Little bug in script for running linux gui solved