import json
import yaml
import os
from yaml.loader import SafeLoader
f = open('prod.json')
moduleSuffix = {}
data = json.load(f)
duplicateFileName = {}
duplicateYamlFile = {}

def convertFileNameDuplicate(s):
	new_s = s[0].lower()
	for c in s[1:]:
		if c.isupper():
			new_s += "-" + c.lower()
		else:
			new_s += c
	return new_s

def convertFileName(s):
	# Initialize the modified string as empty
	modified_s = ""
	# Keep track of whether the previous character was a capital letter
	prev_is_cap = False
	# Loop over each character in the string
	for c in s:
		# Check if the character is a capital letter
		if c.isupper():
			# If the previous character was also a capital letter, just append it to the modified string
			if prev_is_cap:
				modified_s += c.lower()
			# Otherwise, append the lowercase version of the capital letter with an underscore prefix if it is not the first character
			else:
				modified_s += ("-" if len(modified_s) > 0 else "") + c.lower()
			prev_is_cap = True
		# If the character is not a capital letter, just append it to the modified string
		else:
			modified_s += c
			prev_is_cap = False

	if modified_s == "ngvariable.yaml":
		return "ng-variable.yaml"
	elif modified_s == "number-ngvariable.yaml":
		return "number-ng-variable.yaml"
	elif modified_s == "output-ngvariable.yaml":
		return "output-ng-variable.yaml"
	elif modified_s == "secret-ngvariable.yaml":
		return "secret-ng-variable.yaml"
	elif modified_s == "string-ngvariable.yaml":
		return "string-ng-variable.yaml"
	elif modified_s == "prbuild-spec.yaml":
		return "pr-build-spec.yaml"

	elif modified_s == "azure-armrollback-step-node.yaml":
		return "azure-a-r-m-rollback-step-node.yaml"
	elif modified_s == "azure-armrollback-step-info.yaml":
		return "azure-a-r-m-rollback-step-info.yaml"

	elif modified_s == "azure-create-armresource-parameter-file.yaml":
		return "azure-create-a-r-m-resource-parameter-file.yaml"
	elif modified_s == "azure-create-armresource-step-configuration.yaml":
		return "azure-create-a-r-m-resource-step-configuration.yaml"
	elif modified_s == "azure-create-armresource-step-info.yaml":
		return "azure-create-a-r-m-resource-step-info.yaml"
	elif modified_s == "azure-create-armresource-step-node.yaml":
		return "azure-create-a-r-m-resource-step-node.yaml"
	elif modified_s == "azure-create-armresource-step-scope.yaml":
		return "azure-create-a-r-m-resource-step-scope.yaml"
	elif modified_s == "azure-create-bpstep-configuration.yaml":
		return "azure-create-b-p-step-configuration.yaml"
	elif modified_s == "azure-create-bpstep-info.yaml":
		return "azure-create-b-p-step-info.yaml"
	elif modified_s == "azure-create-bpstep-node.yaml":
		return "azure-create-b-p-step-node.yaml"

	elif modified_s == "elastigroup-bgstage-setup-step-info.yaml":
		return "elastigroup-b-g-stage-setup-step-info.yaml"
	elif modified_s == "elastigroup-bgstage-setup-step-node.yaml":
		return "elastigroup-b-g-stage-setup-step-node.yaml"

	elif modified_s == "k8s-bgswap-services-step-info.yaml":
		return "k8s-b-g-swap-services-step-info.yaml"
	elif modified_s == "k8s-bgswap-services-step-node.yaml":
		return "k8s-b-g-swap-services-step-node.yaml"

	elif modified_s == "merge-prstep-info.yaml":
		return "merge-pr-step-info.yaml"
	elif modified_s == "merge-prstep-node.yaml":
		return "merge-pr-step-node.yaml"

	elif modified_s == "tas-bgapp-setup-step-info.yaml":
		return "tas-b-g-app-setup-step-info.yaml"
	elif modified_s == "tas-bgapp-setup-step-node.yaml":
		return "tas-b-g-app-setup-step-node.yaml"


	return modified_s

def fileDirPath(module,expected,newFileNameWithScore):
	if module==expected:
		return newFileNameWithScore
	if (not module or  module == 'pipeline'):
		if expected == 'pie':
			return newFileNameWithScore
	return '../' + expected + '/'+newFileNameWithScore


def saveRefFile(module, newyaml, fileName):
	newFileNameWithScore = convertFileName(fileName)
	print("saveRefFile newyaml[] "+ newFileNameWithScore)
	if os.path.exists('../common/' + newFileNameWithScore):
		print("file exists ../../common/"+ newFileNameWithScore)
		if(newFileNameWithScore+module in duplicateFileName):
			#duplicateFileName[fileName+module].append(newyaml)
			print()
		else:
			duplicateFileName[newFileNameWithScore+module]=[]
			duplicateFileName[newFileNameWithScore+module].append(newyaml)
		return '../../common/' + newFileNameWithScore

	if os.path.exists('common/' + newFileNameWithScore):
		print("file exists ../common/"+ newFileNameWithScore)
		print(os.getcwd())
		if(newFileNameWithScore+module in duplicateFileName):
			#duplicateFileName[fileName+module].append(newyaml)
			print()
		else:
			duplicateFileName[newFileNameWithScore+module]=[]
			duplicateFileName[newFileNameWithScore+module].append(newyaml)
		return '../common/' + newFileNameWithScore

	if os.path.exists('custom/' + newFileNameWithScore):
		# print("file already there "+ newFileNameWithScore)
		if(newFileNameWithScore+module in duplicateFileName):
			#duplicateFileName[fileName+module].append(newyaml)
			print()
		else:
			duplicateFileName[newFileNameWithScore+module]=[]
			duplicateFileName[newFileNameWithScore+module].append(newyaml)
		return fileDirPath(module,'custom', newFileNameWithScore)
	else:
		fileNameWithoutYaml = fileName.removesuffix('.yaml')
		my_function(module,fileNameWithoutYaml)
		return fileDirPath(module, module, newFileNameWithScore)


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
		module = 'custom'
	else:
		module=pathList[2]
	print(pathList)
	print("module name is "+ module)
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
				print("prashant element [] "+ listData[c] + " new path " + savedNewFilePath)
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
					print("prashant element [] "+ yamlData[element] + " new path " + savedNewFilePath)
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
		print(yamlData)
		for element in yamlData:
			if isinstance(element, str):
				if(element == "$ref" and yamlData[element].startswith("#/")):
					print("prashant element [] "+ yamlData[element] + " new path " + savedNewFilePath)
					savedNewFilePath = saveYaml(yamlData[element].split('/'))
					print("prashant element [] "+ yamlData[element] + " new path " + savedNewFilePath)
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
	if(not module or  module == 'pipeline' or module == 'custom'):
		level = data
		directoryPath = 'custom' + '/'
		if not os.path.exists('custom'):
			print('created the custom module')
			os.makedirs('custom')
	else:
		level = data[module]
		directoryPath = module + '/'
		if not os.path.exists(module):
			os.makedirs(module)
	for i in level:
		if i.endswith(prefix):
			fileName = i + '.yaml'
			fileNameWithUnderscore =convertFileName(fileName)
			#print(yaml_data)
			if os.path.exists('../common/' + fileNameWithUnderscore):
				with open('../common/' + fileNameWithUnderscore, 'w') as file:
					yaml.dump(level[i], file, sort_keys=False)
				updatedYamlData = read_yaml('../../common/' + fileNameWithUnderscore)
				title={"title":fileName.removesuffix('.yaml')}
				updatedYamlData={**title,**updatedYamlData}
				updateDescription(updatedYamlData,fileName.removesuffix('.yaml'))
				updateYaml('common/'+fileNameWithUnderscore,updatedYamlData)
			elif os.path.exists('../common/' + fileNameWithUnderscore):
				with open('../common/' + fileNameWithUnderscore, 'w') as file:
					yaml.dump(level[i], file, sort_keys=False)
				updatedYamlData = read_yaml('../common/' + fileNameWithUnderscore)
				title={"title":fileName.removesuffix('.yaml')}
				updatedYamlData={**title,**updatedYamlData}
				updateDescription(updatedYamlData,fileName.removesuffix('.yaml'))
				updateYaml('../common/'+fileNameWithUnderscore,updatedYamlData)
			elif os.path.exists('custom/' + fileNameWithUnderscore):
				print("tere")
				with open('custom/' + fileNameWithUnderscore, 'w') as file:
					yaml.dump(level[i], file, sort_keys=False)
				updatedYamlData = read_yaml('custom/' + fileNameWithUnderscore)
				title={"title":fileName.removesuffix('.yaml')}
				updatedYamlData={**title,**updatedYamlData}
				updateDescription(updatedYamlData,fileName.removesuffix('.yaml'))
				updateYaml('custom/'+fileNameWithUnderscore,updatedYamlData)
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




moduleList = ['','custom','cd','ci','iacm','security','cvng']

for module in moduleList:
	print()
	my_function(module,'StepNode')
f.close()