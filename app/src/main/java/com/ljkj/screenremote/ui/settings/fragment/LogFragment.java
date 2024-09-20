package com.ljkj.screenremote.ui.settings.fragment;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.hjq.toast.Toaster;
import com.ljkj.lib_common.base.fragment.BaseFragment;
import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.bean.LogListBean;
import com.ljkj.lib_common.common.Constants;
import com.ljkj.lib_common.http.api.BaseResponse;
import com.ljkj.lib_common.utils.FileUtils;
import com.ljkj.screenremote.adapter.LogListAdapter;
import com.ljkj.screenremote.databinding.FragmentLogBinding;
import com.ljkj.screenremote.ui.settings.contarct.LogFragmentContract;
import com.ljkj.screenremote.ui.settings.presenter.LogFragmentPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class LogFragment extends BaseFragment<LogFragmentPresenter, FragmentLogBinding> implements LogFragmentContract.LogView {

    public static final String TAG = LogFragment.class.getSimpleName();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private LogListAdapter adapterCan;
    private LogListAdapter adapterOperate;
    private LogListAdapter adapterNav;
    private LogListAdapter adapterCrash;
    private LogListAdapter adapterOther;

    private List<LogListBean> logListCan = new ArrayList<>();
    private List<LogListBean> logListOperate = new ArrayList<>();
    private List<LogListBean> logListNav = new ArrayList<>();
    private List<LogListBean> logListCrash = new ArrayList<>();
    private List<LogListBean> logListOther = new ArrayList<>();

    private List<LogListBean> selectList = new ArrayList<>();

    private FileUtils ft;

    @Override
    protected FragmentLogBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentLogBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        ft = FileUtils.getInstance();
        ft.init(getActivity());

        setupRecyclerViews();
        loadLogFiles();
    }


    @Override
    protected void initData() {
//        createTestFile(ft.CAN_LOG_FILE_PATH, "can");
//        createTestFile(ft.WORK_LOG_FILE_PATH, "work");
//        createTestFile(ft.NAV_LOG_FILE_PATH, "nav");
//        createTestFile(ft.CRASH_LOG_FILE_PATH, "crash");
//        createTestFile(ft.OTHER_LOG_FILE_PATH, "other");
    }

    public void createTestFile(String path, String type) {
        try {
            File testFile = new File(path, "test_log_" + type + ".txt");
            if (!testFile.exists()) {
                boolean created = testFile.createNewFile();
                if (created) {
                    FileOutputStream fos = new FileOutputStream(testFile);
                    fos.write("测试文件".getBytes());
                    fos.close();
                }
            }

            LogListBean testLogBean = new LogListBean();
            testLogBean.setName("test_log_" + type + ".txt");
            testLogBean.setPath(testFile.getAbsolutePath());
            testLogBean.setSelect(false);
            logListOther.add(testLogBean);  // 添加到 Other 列表中
            adapterOther.setList(logListOther);  // 更新适配器
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "创建测试文件" +
                    "失败: " + e.getMessage());
        }
    }


    @Override
    protected void initListener() {
        binding.rlCan.setOnClickListener(v -> {
            if (binding.recyclerviewCan.getVisibility() == View.VISIBLE) {
                binding.recyclerviewCan.setVisibility(View.GONE);
            } else {
                binding.recyclerviewCan.setVisibility(View.VISIBLE);
            }
        });
        binding.rlOperate.setOnClickListener(v -> {
            if (binding.recyclerviewOperate.getVisibility() == View.VISIBLE) {
                binding.recyclerviewOperate.setVisibility(View.GONE);
            } else {
                binding.recyclerviewOperate.setVisibility(View.VISIBLE);
            }
        });
        binding.rlNav.setOnClickListener(v -> {
            if (binding.recyclerviewNav.getVisibility() == View.VISIBLE) {
                binding.recyclerviewNav.setVisibility(View.GONE);
            } else {
                binding.recyclerviewNav.setVisibility(View.VISIBLE);
            }
        });
        binding.rlCrash.setOnClickListener(v -> {
            if (binding.recyclerviewCrash.getVisibility() == View.VISIBLE) {
                binding.recyclerviewCrash.setVisibility(View.GONE);
            } else {
                binding.recyclerviewCrash.setVisibility(View.VISIBLE);
            }
        });
        binding.rlOther.setOnClickListener(v -> {
            if (binding.recyclerviewOther.getVisibility() == View.VISIBLE) {
                binding.recyclerviewOther.setVisibility(View.GONE);
            } else {
                binding.recyclerviewOther.setVisibility(View.VISIBLE);
            }
        });

        binding.btnUpload.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (selectList.isEmpty()) {
                    Toaster.show("请选择日志文件");
                    return;
                }
                List<File> selectFiles = new ArrayList<>();
                for (LogListBean bean : selectList) {
                    selectFiles.add(new File(bean.path));
                }

                Disposable disposable = ft.compressFiles(getActivity(), selectFiles, "logs.zip")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                compressedFile -> {
                                    Log.e(TAG, "压缩文件成功");

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("uv", AppUtils.getAppVersionCode());
                                    map.put("lv", "OTA_VERSION");
                                    map.put("cv", "REMOTE_CONTROL_OTA_VERSION");
                                    map.put("rv", "RTK_VERSION");
                                    String jsonString = GsonUtils.toJson(map);

                                    mPresenter.uploadLog(getActivity(), Constants.SN, "5903", compressedFile, "logs.zip", jsonString);
                                },
                                throwable -> {
                                    Log.e(TAG, "压缩文件失败", throwable);
                                }
                        );
                compositeDisposable.add(disposable);
            }
        });
    }


    private void setupRecyclerViews() {
        LinearLayoutManager layoutManagerCan = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManagerOperate = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManagerNav = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManagerCrash = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManagerOther = new LinearLayoutManager(getActivity());

        adapterCan = new LogListAdapter(this);
        adapterOperate = new LogListAdapter(this);
        adapterNav = new LogListAdapter(this);
        adapterCrash = new LogListAdapter(this);
        adapterOther = new LogListAdapter(this);

        binding.recyclerviewCan.setLayoutManager(layoutManagerCan);
        binding.recyclerviewCan.setAdapter(adapterCan);

        binding.recyclerviewOperate.setLayoutManager(layoutManagerOperate);
        binding.recyclerviewOperate.setAdapter(adapterOperate);

        binding.recyclerviewNav.setLayoutManager(layoutManagerNav);
        binding.recyclerviewNav.setAdapter(adapterNav);

        binding.recyclerviewCrash.setLayoutManager(layoutManagerCrash);
        binding.recyclerviewCrash.setAdapter(adapterCrash);

        binding.recyclerviewOther.setLayoutManager(layoutManagerOther);
        binding.recyclerviewOther.setAdapter(adapterOther);
    }

    private void loadLogFiles() {
        logListCan.addAll(ft.getSonNode(new File(Constants.CAN_LOG_FILE_PATH), 1));
        adapterCan.setList(logListCan);

        logListOperate.addAll(ft.getSonNode(new File(Constants.WORK_LOG_FILE_PATH), 2));
        adapterOperate.setList(logListOperate);

        logListNav.addAll(ft.getSonNode(new File(Constants.NAV_LOG_FILE_PATH), 3));
        adapterNav.setList(logListNav);

        logListCrash.addAll(ft.getSonNode(new File(Constants.CRASH_LOG_FILE_PATH), 4));
        adapterCrash.setList(logListCrash);

        logListOther.addAll(ft.getSonNode(new File(Constants.OTHER_LOG_FILE_PATH), 5));
        adapterOther.setList(logListOther);
    }

    @SuppressLint("LongLogTag")
    public void operationLogFiles(LogListBean logListBean) {
        if (logListBean.isSelect()) {
            if (!selectList.contains(logListBean)) {
                selectList.add(logListBean);
            }
        } else {
            selectList.remove(logListBean);
        }
        Log.d(TAG, "select files: " + selectList.size());
    }


    @SuppressLint("NotifyDataSetChanged")
    private void clearSelectedFiles() {
        selectList.clear();

        for (LogListBean log : logListOther) {
            log.setSelect(false);
        }
        adapterCan.notifyDataSetChanged();
        adapterOperate.notifyDataSetChanged();
        adapterNav.notifyDataSetChanged();
        adapterCrash.notifyDataSetChanged();
        adapterOther.notifyDataSetChanged();

        Log.d(TAG, "选择的日志文件已清空");
    }


    @Override
    public void showUpLoadSuccess(BaseResponse<LogBean> response) {
        Toaster.show("上传成功");
    }

    @Override
    public void showUpLoadFailed(BaseResponse<LogBean> response) {
        Toaster.show("上传失败" + response.getErr_msg());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }
}
