from setuptools import setup
from setuptools.command.install import install
import os

# only needed for readthedocs.io

class PreProcess(install):
  def run(self):
    install.run(self)
    print("sed -i \"s/__VERSION__/" + os.environ['READTHEDOCS_VERSION'] + "/g\" docs/*.md")
    os.system("sed -i \"s/__VERSION__/" + os.environ['READTHEDOCS_VERSION'] + "/g\" docs/*.md")

setup(
  name="docs",
  cmdclass={'install': PreProcess},
)
