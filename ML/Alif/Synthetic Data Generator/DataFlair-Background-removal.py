from PIL import Image
import os

source_dir = r'D:\archive_2\dataset\Not Ready Yet\Rotten_Papaya\Remove bg'
image_file = os.listdir(source_dir)
for index, image in enumerate(image_file):
    path = source_dir + '\\' + image
    im1 = Image.open(path)
    rgb_im = im1.convert('RGB')
    output_path = r'D:\archive_2\dataset\Not Ready Yet\Rotten_Papaya\Raw' + '\\' + 'image{}.jpg'.format(index)
    rgb_im.save(output_path)
