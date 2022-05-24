import os
from subprocess import Popen, PIPE

folderToCheck = '/Dataset Fix/Fresh_Waterguava'
fileExtension = '.jpg'

def checkImage(fn):
  proc = Popen(['identify', '-verbose', fn], stdout=PIPE, stderr=PIPE)
  out, err = proc.communicate()
  exitcode = proc.returncode
  return exitcode, out, err

for directory, subdirectories, files, in os.walk(folderToCheck):
  for file in files:
    if file.endswith(fileExtension):
      filePath = os.path.join(directory, file)
      code, output, error = checkImage(filePath)
      if str(code) !="0" or str(error, "utf-8") != "":
        print("ERROR " + filePath)
      else:
        print("OK " + filePath)

print("-------------- DONE --------------")