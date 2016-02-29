# -*- encoding: utf-8 -*-

import bluetooth
import os
import time
import signal
import dbus

pidHijoServicio=0
target_name=""
target_address=None
number_devices=0
dispositivos=[]
class Escanear(object):
	"""docstring for IdentificaMiDireccion"""
	def __init__(self, name):
	
		self.name = name

	def Escaneo(self):
		global number_devices
		global pidHijoServicio
		global pidHijoScaner
		global dispositivos
		try:
			if pidHijoScaner==0:
				while 1:
					exist=0
					nearby_devices=bluetooth.discover_devices(duration=8, lookup_names=True, flush_cache=True, lookup_class=False)
					for name in nearby_devices:
						for dispositivo in dispositivos:
							if name==dispositivo:
								exist=1
						if exist==0:
							dispositivos.append(name)

							number_devices=number_devices+1
							os.kill(pidHijoServicio,signal.SIGUSR1)
						exist=0
					for dispositivo in dispositivos:
						print dispositivo
					print number_devices

					time.sleep(6)
			pass
		except Exception, e:
			print "Error al escanear"
			raise

	def Servicio(self):
		global pidHijoServicio
		global pidHijoScaner
		if pidHijoServicio==0:
			while 1:
				signal.signal(signal.SIGUSR1,cosa)
				signal.pause()
				time.sleep(5)
				



def cosa(signum,frame):
	print "Recibida la señal"


pidpadre=os.getpid()
escaneo=Escanear(target_name)
pidHijoScaner=os.fork()
pidHijoServicio=os.fork()
escaneo.Servicio()
escaneo.Escaneo()
time.sleep(700)

