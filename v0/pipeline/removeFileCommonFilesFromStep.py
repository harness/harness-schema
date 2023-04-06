import yaml
import os

def my_funciton(path):
	currentDir = os.getcwd()
	fileList = os.listdir(path)
	for filename in fileList:
		if(filename.endswith(".yaml")):
			if(os.path.exists('steps/common/'+filename)):
				os.remove('steps/common/'+filename)
				print("file deleted"+filename)

my_funciton('common')
