# -*- coding: utf-8 -*-
#!/usr/bin/env python
from os import listdir
secretos={'1.- Ejercicio 1: Enviar Señales':'<***>','2.- Ejercicio 2: Enviar Señales':'<***>','3.- Ejercicio 3: Enviar Señales':'<***>'}

class Monitor(object):

	def __init__(self, DNI):
		
		self.DNI = DNI
	def MostrarSecretos(self):
		for i in secretos:
			print (i+secretos[i])

contador=0
DNI=raw_input("Introduce el DNI")
for file in listdir("."):
	if(file==DNI):
		contador=1
if contador==1:
	print "El fichero con su DNI es correcto"
else:
	print "Cree el fichero"