package hopepower.com.simplemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView fileList;//当前文件目录
    private String currentPath;//当前路径
    private TextView textView1;//显示当前路径的TextView
    private File[] files;
    private int[] image = {R.drawable.file,R.drawable.folder};
    private SimpleAdapter simpleAdapter;
    private Button backRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[] { Manifest.permission. WRITE_EXTERNAL_STORAGE}, 1);
        }//判断是否有读取存储空间的权限
        fileList = (ListView) findViewById(R.id.file_list);
        textView1 = (TextView) findViewById(R.id.current_path_text);
        backRoot = (Button) findViewById(R.id.back_root);
        init(Environment.getExternalStorageDirectory());
        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击filelist中成员的点击事件
                String folder = ((TextView)view.findViewById(R.id.file_name)).getText().toString();
                try {
                    File file = new File(currentPath + '/' + folder);
                    init(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        backRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init(Environment.getExternalStorageDirectory());
            }
        });
    }

    public void init(File file) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            files = file.listFiles();
            if(!files.equals(null)) {
                currentPath = file.getPath();
                textView1.setText("当前路径为：" + currentPath);
                List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
                for(int i = 0; i < files.length; i++) {
                    Map<String, Object> maps = new HashMap<String, Object>();
                    if(files[i].isFile()) {
                        maps.put("image",image[0]);
                    } else {
                        maps.put("image",image[1]);
                    }
                    maps.put("filename",files[i].getName());
                    list.add(maps);
                }
                simpleAdapter = new SimpleAdapter(this,list,R.layout.list_file_cell,
                        new String[] {"image","filename"},new int[] {R.id.file_image,R.id.file_name});
                fileList.setAdapter(simpleAdapter);
            } else {
                Toast.makeText(this,"该文件夹为空",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
                }
        }
    }

}
