import yaml
import os

def my_funciton(path):
	currentDir = os.getcwd()
	fileList = os.listdir(path)
	for filename in fileList:
		if(filename.endswith(".yaml")):
			if(os.path.exists('steps/pie/'+filename)):
				os.remove('steps/pie/'+filename)
				print("file deleted"+filename)

my_funciton('common')
