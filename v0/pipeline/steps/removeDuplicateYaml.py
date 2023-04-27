import os
import shutil
import hashlib

"""
1. This script will move the common file in a directory to a common module. References will have to be changed manually
2. Yamls like - background-step-node.yaml, background-step-info.yaml, run-step-info.yaml, run-step-node.yaml were moved manually

"""
def find_duplicates(rootdir):
    """
    Find duplicate files in the given directory tree
    :param rootdir: Root directory to start the search from
    :return: A dictionary of duplicate files with their full paths
    """
    dup_files = {}
    for foldername, subfolders, filenames in os.walk(rootdir):
        for filename in filenames:
            file_path = os.path.join(foldername, filename)
            if os.path.isfile(file_path):
                hash_value = hashlib.sha256(open(file_path, 'rb').read()).hexdigest()
                if hash_value in dup_files:
                    print(filename)
                    dup_files[hash_value].append(file_path)
                else:
                    dup_files[hash_value] = [file_path]
    return {k: v for k, v in dup_files.items() if len(v) > 1}

def move_files_to_common(dup_files, common_dir):
    """
    Move duplicate files to common directory and update references
    :param dup_files: Dictionary containing duplicate files with their full paths
    :param common_dir: Directory where the duplicate files will be moved
    """
    for hash_value, files in dup_files.items():
        for i, file_path in enumerate(files):
            new_file_path = os.path.join(common_dir, os.path.basename(file_path))
            if i > 0:
                # Move the duplicate file to the common directory
                if not os.path.exists(new_file_path):
                    shutil.move(file_path, new_file_path)

                    # Update the references for the moved file
                    for foldername, subfolders, filenames in os.walk(rootdir):
                        for filename in filenames:
                            if filename == os.path.basename(file_path):
                                file_path = os.path.join(foldername, filename)
                                with open(file_path, 'r') as f:
                                    content = f.read()
                                with open(file_path, 'w') as f:
                                    f.write(content.replace(file_path, new_file_path))

if __name__ == '__main__':
    rootdir = '.'
    common_dir = './common'
    dup_files = find_duplicates(rootdir)
    move_files_to_common(dup_files, common_dir)
