import os

# Define the list of directories to search
dirs_to_search = ['./pie', './cvng', './common', './ci', './cd']

# Initialize an empty list to store the file paths
file_paths = []

# Loop over each directory in the list
for directory in dirs_to_search:
    # Loop over each file in the directory
    for file_name in os.listdir(directory):
        # Check if the file name ends with 'step-node.yaml'
        if file_name.endswith('step-node.yaml'):
            # If so, construct the full file path and append '../pipeline/steps/' to it
            full_file_path = os.path.join(directory, file_name)
            file_paths.append(os.path.join('-', '..', 'pipeline', 'steps/', os.path.relpath(full_file_path, './')))

# Print each file path on a new line
for file_path in file_paths:
    print(file_path)
