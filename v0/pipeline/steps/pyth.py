import json
import yaml
import os
import difflib
from yaml.loader import SafeLoader
# Opening JSON file
f = open('prod.json')
moduleSuffix = {}
# returns JSON object as
# a dictionary
data = json.load(f)
duplicateFileName = {}
duplicateYamlFile = {}

def convertFileName(s):
	new_s = s[0].lower()
	for c in s[1:]:
		if c.isupper():
			new_s += "-" + c.lower()
		else:
			new_s += c
	return new_s

def fileDirPath(module,expected,newFileNameWithScore):
	if module==expected:
		return newFileNameWithScore
	if (not module or  module == 'pipeline'):
		if expected == 'common':
			return newFileNameWithScore
	return '../' + expected + '/'+newFileNameWithScore


def saveRefFile(module, newyaml, fileName):
	newFileNameWithScore = convertFileName(fileName)
	if os.path.exists('common/' + newFileNameWithScore):
		# print("file already there "+ newFileNameWithScore)
		if(newFileNameWithScore+module in duplicateFileName):
			#duplicateFileName[fileName+module].append(newyaml)
			print()
		else:
			duplicateFileName[newFileNameWithScore+module]=[]
			duplicateFileName[newFileNameWithScore+module].append(newyaml)
		return fileDirPath(module,'common', newFileNameWithScore)
	else:
		fileNameWithoutYaml = fileName.removesuffix('.yaml')
		my_function(module,fileNameWithoutYaml)
		return fileDirPath(module, module, newFileNameWithScore)
	# with open(module + '/'+ fileName, 'w') as file:
	# 	yaml.dump(newyaml, file, sort_keys=False)
	# 	return './' + module + '/'+fileName


def getYamlFromPathList(pathList):
	if(len(pathList)<=2):
		print("something is wrong")
	index = 2
	yaml = data
	while index<len(pathList):
		yaml = yaml[pathList[index]]
		index = index + 1

	return yaml

def saveYaml(pathList):
	newyaml = getYamlFromPathList(pathList)
	if len(pathList)<=3:
		module = 'common'
	else:
		module=pathList[2]
	fileName = pathList[len(pathList)-1] + '.yaml'
	return saveRefFile(module,newyaml,fileName)

def handleListElement(listData):
	c=0
	for listElement in listData:
		if isinstance(listElement,dict):
			iterateOverMap(listElement)
		if isinstance(listElement,str):
			if(listElement == "$ref" and listData[c].startswith("#/")):
				pathList = listData[c].split('/')
				savedNewFilePath=saveYaml(pathList)
				listData[c]=savedNewFilePath
				return savedNewFilePath
		if isinstance(listElement,list):
			return handleListElement(listElement)
		c=c+1


def iterateOverMap(yamlData):
	if(isinstance(yamlData, dict)):
		for element in yamlData:
			if isinstance(element, str):
				# print(element)
				if(element == "$ref" and yamlData[element].startswith("#/")):
					savedNewFilePath = saveYaml(yamlData[element].split('/'))
					yamlData[element] = savedNewFilePath
			#print(element)
			if isinstance(yamlData[element],dict):
				iterateOverMap(yamlData[element])
			if isinstance(yamlData[element],list):
				handleListElement(yamlData[element])



def read_yaml(path):
	#print(path)
	with open(path) as yamlField:
		yamlData = yaml.load(yamlField, Loader=SafeLoader)
		for element in yamlData:
			if isinstance(element, str):
				if(element == "$ref" and yamlData[element].startswith("#/")):
					savedNewFilePath = saveYaml(yamlData[element].split('/'))
					yamlData[element] = savedNewFilePath
			if isinstance(yamlData[element], dict):
				iterateOverMap(yamlData[element])
			if isinstance(yamlData[element],list):
				handleListElement(yamlData[element])

	# print("File done ")
	return yamlData

def updateDescription(updatedYamlData, fileName):
	if("properties" not in updatedYamlData):
		updatedYamlData["properties"]={}
	properties = updatedYamlData["properties"]
	if "description" not in properties:
		properties["description"]={}
	description = properties["description"]
	description["desc"]='This is the description for '+fileName
	print(properties)

def updateYaml(path,yamlContent):
	with open(path, 'w') as file2:
		#print(yamlContent)
		yaml.dump(yamlContent,file2,sort_keys=False)
		file2.close
# Iterating through the json
# list
def my_function(module,prefix):
	level = data
	# print(prefix)
	if module+prefix in moduleSuffix:
		return
	moduleSuffix[module+prefix]=1
	if(not module or  module == 'pipeline' or module == 'common'):
		level = data
		directoryPath = 'common' + '/'
		if not os.path.exists('common'):
			print('created the common module')
			os.makedirs('common')
	else:
		level = data[module]
		directoryPath = module + '/'
		if not os.path.exists(module):
			os.makedirs(module)
	for i in level:
		if i.endswith(prefix):
			#py_data = json.loads(data[i])
			yaml_data = yaml.dump(level[i])
			fileName = i + '.yaml'
			fileNameWithUnderscore =convertFileName(fileName)
			#print(yaml_data)
			if os.path.exists('common/' + fileNameWithUnderscore):
				#create a test file to compare
				#print("file already in common - "+fileNameWithUnderscore)
				updatedYamlData = read_yaml('common/' + fileNameWithUnderscore)
				updateYaml('common/'+fileNameWithUnderscore,updatedYamlData)
			#print()
			else:
				with open(directoryPath + fileNameWithUnderscore, 'w') as file:
					yaml.dump(level[i], file, sort_keys=False)
					updatedYamlData = read_yaml(directoryPath + fileNameWithUnderscore)
					file.close()
					title={"title":fileName.removesuffix('.yaml')}
					updatedYamlData={**title,**updatedYamlData}
					updateDescription(updatedYamlData,fileName.removesuffix('.yaml'))
					updateYaml(directoryPath+fileNameWithUnderscore,updatedYamlData)




moduleList = ['', 'pipeline','ci', 'cd', 'cvng', 'security', 'approval', 'cf', 'custom']
# moduleList = ['']

for module in moduleList:
	print()
	my_function(module,'StepNode')
print("duplicate files are")
for keys in duplicateFileName:
	# print("yaml for "+ keys)
	for element in duplicateFileName[keys]:
		print()
f.close()