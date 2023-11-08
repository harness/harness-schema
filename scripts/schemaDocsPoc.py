from json_schema_for_humans.generate import generate_from_filename
from json_schema_for_humans.generation_configuration import GenerationConfiguration

config = GenerationConfiguration(copy_css=False, expand_buttons=True)


 # json-schema-for-humans - Library

# generate_from_filename("waitStepIndividualSchemaV0.json", "waitStepIndividualSchemaV0.html", config=config)

generate_from_filename("exampleJsons/customStageTemplateV0.json", "generatedDocs/customStageTemplateV0.html", config=config)

generate_from_filename("exampleJsons/waitStepTemplateV0.json", "generatedDocs/waitStepTemplateV0.html", config=config)
