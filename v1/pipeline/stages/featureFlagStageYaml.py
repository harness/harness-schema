import json
import yaml
import os
from yaml.loader import SafeLoader
f = open('../steps/prod.json')
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

	elif modified_s == "cvngstep-info.yaml":
		return "cvng-step-info.yaml"
	elif modified_s == "cvverify-step-node.yaml":
		return "cv-verify-step-node.yaml"

	elif modified_s == "amiartifact-config.yaml":
		return "ami-artifact-config.yaml"
	elif modified_s == "amifilter.yaml":
		return "ami-filter.yaml"
	elif modified_s == "amitag.yaml":
		return "ami-tag.yaml"

	elif modified_s == "custom-deployment-connector-ngvariable.yaml":
		return "custom-deployment-connector-ng-variable.yaml"
	elif modified_s == "custom-deployment-ngvariable.yaml":
		return "custom-deployment-ng-variable.yaml"
	elif modified_s == "custom-deployment-number-ngvariable.yaml":
		return "custom-deployment-number-ng-variable.yaml"


	elif modified_s == "custom-deployment-secret-ngvariable.yaml":
		return "custom-deployment-secret-ng-variable.yaml"
	elif modified_s == "custom-deployment-string-ngvariable.yaml":
		return "custom-deployment-string-ng-variable.yaml"
	elif modified_s == "custom-deployment-number-ngvariable.yaml":
		return "custom-deployment-number-ng-variable.yaml"


	elif modified_s == "k8-sdirect-infrastructure.yaml":
		return "k8-s-direct-infrastructure.yaml"
	elif modified_s == "ngvariable-override-set-wrapper.yaml":
		return "ng-variable-override-set-wrapper.yaml"
	elif modified_s == "ngvariable-override-sets.yaml":
		return "ng-variable-override-sets.yaml"


	return modified_s


def fileDirPath(module,expected,newFileNameWithScore):
	if module==expected:
		return newFileNameWithScore
	if (not module or  module == 'pipeline'):
		if expected == 'common':
			return newFileNameWithScore
	return '../' + expected + '/'+newFileNameWithScore


def saveRefFile(module, newyaml, fileName):
	newFileNameWithScore = convertFileName(fileName)
	if os.path.exists('../common/' + newFileNameWithScore):
		# print("file already there "+ newFileNameWithScore)
		if(newFileNameWithScore+module in duplicateFileName):
			#duplicateFileName[fileName+module].append(newyaml)
			print()
		else:
			duplicateFileName[newFileNameWithScore+module]=[]
			duplicateFileName[newFileNameWithScore+module].append(newyaml)
		return '../../common/' + newFileNameWithScore

	elif os.path.exists('../steps/common/' + newFileNameWithScore):
		return '../../steps/common/' + newFileNameWithScore

	elif os.path.exists('../steps/custom/' + newFileNameWithScore):
		return '../../steps/custom/' + newFileNameWithScore

	elif os.path.exists('../steps/ci/' + newFileNameWithScore):
		return '../../steps/ci/' + newFileNameWithScore
	elif os.path.exists('../steps/cvng/' + newFileNameWithScore):
		return '../../steps/cvng/' + newFileNameWithScore

	elif os.path.exists('../steps/security/' + newFileNameWithScore):
		return '../../steps/security/' + newFileNameWithScore

	elif os.path.exists('../steps/iacm/' + newFileNameWithScore):
		return '../../steps/iacm/' + newFileNameWithScore

	elif os.path.exists('../steps/cd/' + newFileNameWithScore):
		return '../../steps/cd/' + newFileNameWithScore

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
	if len(pathList)<=2:
		module = 'custom'
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
					print("prashant check here - "+ yamlData[element].startswith("#/"))
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

def updateYaml(path,yamlContent):
	with open(path, 'w') as file2:
		yaml.dump(yamlContent,file2,sort_keys=False)
		file2.close
# Iterating through the json
# list
def my_function(module,prefix):
	level = data
	if module+prefix in moduleSuffix:
		return
	moduleSuffix[module+prefix]=1
	if(not module or  module == 'pipeline' or module == 'common' or module=='pie'):
		level = data["pipeline"]
		directoryPath = 'custom' + '/'
		if not os.path.exists('custom'):
			os.makedirs('custom')
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
			if os.path.exists('../common/' + fileNameWithUnderscore):
				#create a steps file to compare
				updatedYamlData = read_yaml('../common/' + fileNameWithUnderscore)
				updateYaml('../common/'+fileNameWithUnderscore,updatedYamlData)

			elif os.path.exists('../steps/common/' + fileNameWithUnderscore):
				return '../../steps/common/' + fileNameWithUnderscore

			elif os.path.exists('../steps/custom/' + fileNameWithUnderscore):
				updatedYamlData = read_yaml('../steps/custom/' + fileNameWithUnderscore)
				updateYaml('../steps/custom/'+fileNameWithUnderscore,updatedYamlData)

			elif os.path.exists('../steps/ci/' + fileNameWithUnderscore):
				updatedYamlData = read_yaml('../steps/ci/' + fileNameWithUnderscore)
				updateYaml('../steps/ci/'+fileNameWithUnderscore,updatedYamlData)

			elif os.path.exists('../steps/cd/' + fileNameWithUnderscore):
				updatedYamlData = read_yaml('../steps/cd/' + fileNameWithUnderscore)
				updateYaml('../steps/cd/'+fileNameWithUnderscore,updatedYamlData)

			elif os.path.exists('../steps/iacm/' + fileNameWithUnderscore):
				updatedYamlData = read_yaml('../steps/iacm/' + fileNameWithUnderscore)
				updateYaml('../steps/iacm/'+fileNameWithUnderscore,updatedYamlData)

			elif os.path.exists('../steps/cvng/' + fileNameWithUnderscore):
				updatedYamlData = read_yaml('../steps/cvng/' + fileNameWithUnderscore)
				updateYaml('../steps/cvng/'+fileNameWithUnderscore,updatedYamlData)

			elif os.path.exists('../steps/security/'+fileNameWithUnderscore):
				updatedYamlData= read_yaml('../steps/security/' + fileNameWithUnderscore)
				updateYaml('../steps/security/'+fileNameWithUnderscore,updatedYamlData)
			else:
				with open(directoryPath + fileNameWithUnderscore, 'w') as file:
					yaml.dump(level[i], file, sort_keys=False)
					print("calling read yaml for "+ fileNameWithUnderscore)
					updatedYamlData = read_yaml(directoryPath + fileNameWithUnderscore)
					file.close()
					title={"title":fileName.removesuffix('.yaml')}
					updatedYamlData={**title,**updatedYamlData}
					updateDescription(updatedYamlData,fileName.removesuffix('.yaml'))
					updateYaml(directoryPath+fileNameWithUnderscore,updatedYamlData)



moduleList = ['iacm']
# moduleList = ['']

for module in moduleList:
	print()
	my_function(module,'IACMStageNode')
f.close()