import json

def create_markdown_file(json_file_path, markdown_file_path):
	with open(json_file_path, 'r') as json_file:
		data = json.load(json_file)

	with open(markdown_file_path, 'w') as markdown_file:
		for key, value in data.items():
			markdown_file.write(f"## {key}\n")
			if isinstance(value, list):
				for item in value:
					markdown_file.write(f"- {item}\n")
			else:
				markdown_file.write(f"{value}\n")


create_markdown_file('pipeline.json','pipelineReadme.md')