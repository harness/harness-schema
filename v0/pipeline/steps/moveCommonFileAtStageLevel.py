import yaml
import os

# Recursive function to print all keys in a dictionary or list
def createFileOutsideTestModule(path,module):
	if(path.startswith("../../")):
		return path
	pathArray = path.split('/')
	if(len(pathArray)>=2 and os.path.exists(path)):
		with open(path) as file1:
			oldYaml = yaml.safe_load(file1)
			newDirectory = "../"+pathArray[0]+'/'+'common/'+pathArray[len(pathArray)-1]
			print(os.getcwd())
			with open(newDirectory,'w') as newFile:
				yaml.dump(oldYaml, newFile, sort_keys=False)
				return newDirectory

	if(len(pathArray)==1 and module == 'pie'):
		print(module+ " "+ path)
		if(os.path.exists('../../common/'+path)):
			print("yes")
			return '../../common/'+path


	return path


def print_keys(data,module):
	if isinstance(data, dict):
		for key in data:
			if (key == "$ref"):
				newPath = createFileOutsideTestModule(data[key],module)
				if(module == 'pie'):
					print(data[key] + ' '+newPath)
				data[key]=newPath
			print_keys(data[key],module)
	elif isinstance(data, list):
		for item in data:
			print_keys(item,module)
	return data

# Load your YAML file into a Python data structure
# with open('common/barrier-step-node.yaml') as file:
#     data = yaml.safe_load(file)

# Call the recursive function to print all keys
# print_keys(data)


def func2(module, filename):
	print(filename)
	with open(filename) as file:
		data = yaml.safe_load(file)
		updatedData = print_keys(data,module)
		if (data!=updatedData):
			print(filename)
		file.close()
	with open(filename,'w') as file:
		yaml.dump(updatedData,file,sort_keys=False)

def my_funciton(path):
	currentDir = os.getcwd()
	for filename in os.listdir(path):
		if os.path.isfile(os.path.join(path, filename)):
			print(os.getcwd())
			os.chdir(os.getcwd()+'/'+path)
			func2(path,filename)
			os.chdir(currentDir)
	os.chdir(currentDir)

#common is added in the add deliberately
moduleList = ['ci', 'cd', 'cvng', 'security', 'approval', 'cf', 'custom','pie']

for element in moduleList:
	my_funciton(element)
	print()
