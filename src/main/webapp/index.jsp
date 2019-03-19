<%@ page pageEncoding="UTF-8"%>
<html>
<body>

<h2>Hello World!</h2>

<div>
    SpringMVC上传文件
    <form name="form1" action="/admin/product/upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file"/>
        <input type="submit" value="SpringMVC上传文件"/>
    </form>
</div>

<div>
    富文本图片上传文件
    <form name="form2" action="/admin/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file"/>
        <input type="submit" value="富文本图片上传文件"/>
    </form>
</div>

</body>
</html>
