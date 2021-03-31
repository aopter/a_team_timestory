package com.example.timestory.ability.problem.slice;


import com.example.timestory.ResourceTable;
import com.example.timestory.Utils.HmOSImageLoader;
import com.example.timestory.Utils.ToastUtil;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.Problem;
import com.example.timestory.entity.problem.ProblemCheckAnswer;
import com.example.timestory.entity.problem.ProblemLinkLine;
import com.example.timestory.entity.problem.ProblemSelect;
import com.example.timestory.entity.problem.ProblemgetOrder;
import com.google.gson.Gson;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectProblemSlice extends AbilitySlice {
    //    左右屏幕的滑动事件
    final int RIGHT = 0;
    final int LEFT = 1;
    private boolean isGetAnswer = false;
    private Text mTvUserE;//经验
    private Text mTvUserC;//积分
    private Text mTvTitle;//标题
    private Image mIvOptionA;//选项1图片
    private Text mTvOptionA;//选项一 文字
    private Image mIvCheckA;//选项一 对勾图
    private Image mIvOptionB;
    private Text mTvOptionB;
    private Image mIvCheckB;
    private Image mIvOptionC;
    private Text mTvOptionC;
    private Image mIvCheckC;
    private Image mIvOptionD;
    private Text mTvOptionD;
    private Image mIvCheckD;
    private Text mProblemAnswer;
    private Text mProblemSave;
    private Text mProblemUp;
    private Text mProblemP;
    private Text mProblemN;
    private Image[] imageViewsCheck;
    private String dynastyId;//朝代id
    Problem cProblem;//当前题目
    ProblemSelect problemSelect;
    ProblemgetOrder problemgetOrder;
    ProblemLinkLine problemLinkLine;
    private DirectionalLayout mDlXuanA;
    private DirectionalLayout mDlXuanB;
    private DirectionalLayout mDlXuanC;
    private DirectionalLayout mDlXuanD;
    private List<Problem> myProblems;
    private String before;//跳转前页面代号
    private int cIndex = 0;//当前索引

    private Gson gson;//json解析
    private OkHttpClient okHttpClient;//网络访问请求对象
    private TaskDispatcher parallelTaskDispatcher;//并发任务分发器
    public static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD00101, "ProblemSlice");


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_problem_select);
//        查找组件
        findComponents();
        initGson();

//      跳转的参数
        before = intent.getStringParam("before");
        dynastyId = intent.getStringParam("dynastyId");

        if (before.equals("types"))//类型页
        {
            HiLog.info(SelectProblemSlice.LABEL_LOG, "朝代页跳转");
            getProblemFromServer(1);//下载题目
        } else {//收藏页跳转
            HiLog.info(SelectProblemSlice.LABEL_LOG, "收藏页跳转");
            mProblemSave.setText("已收藏");
//           隐藏上一题 下一题
            mProblemP.setVisibility(Component.INVISIBLE);
            mProblemN.setVisibility(Component.INVISIBLE);

            cIndex = intent.getIntParam("position", 0);
            problemSelect = (ProblemSelect) intent.getSerializableParam("problem");
            cProblem.setProblemId(problemSelect.getProblemId());
            cProblem.setProblemDetails(problemSelect.getProblemDetails());
            cProblem.setGetAnswer(true);
            cProblem.setCollection(true);
//           cProblem //显示
            mProblemUp.setVisibility(Component.HIDE);
            mTvTitle.setText(problemSelect.getTitle());
            mTvOptionA.setText(problemSelect.getOptionA());
            mTvOptionB.setText(problemSelect.getOptionB());
            mTvOptionC.setText(problemSelect.getOptionC());
            mTvOptionD.setText(problemSelect.getOptionD());
            String url = ServiceConfig.SERVICE_ROOT + "/img/";
            HiLog.info(LABEL_LOG, "访问的路径" + url + problemSelect.getOptionApic());
            HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionApic()).into(mIvOptionA);
            HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionBpic()).into(mIvOptionB);
            HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionCpic()).into(mIvOptionC);
            HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionDpic()).into(mIvOptionD);

        }
        //初始化并发任务分发器
        parallelTaskDispatcher = createParallelTaskDispatcher("ProblemPageParallelTaskDispatcher", TaskPriority.DEFAULT);
//        答题设置监听器
        setListener();
    }

    //    上一题 下一题
    private void doResult(int action) {
        for (int i = 0; i < 4; ++i) {// 清空图片
            imageViewsCheck[i].setPixelMap(0);
        }
        switch (action) {
            case LEFT:
                cIndex += 1;
                if (before.equals("info")) {
                    return;
                }
                if (myProblems.size() > cIndex) {//下一道
                    getBeforeOrNextProblem();//显示
                    return;
                } else {

                    getProblemFromServer(1);
                }

                break;
            case RIGHT:
                cIndex -= 1;
                if (before.equals("info")) {
                    return;
                }
                if (cIndex < 0) {//上一道
                    cIndex = 0;
//                   提示不能滑动
                    ToastUtil.showSickToast(SelectProblemSlice.this,"不能再滑啦");
//                    Toast.makeText(getApplicationContext(), "不能再滑啦", Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    显示
                    getBeforeOrNextProblem();//显示上一道题目
                }
                break;
        }
    }


    //    获取上下一道题目
    private void getBeforeOrNextProblem() {
        Problem problem = myProblems.get(cIndex);
        cProblem = problem;
//        上一题 下一题是否收藏 是否答过
        if (cProblem.isGetAnswer()) {//答过
//            显示解析
            mProblemAnswer.setVisibility(Component.VISIBLE);//显示
        } else {//没有答过
            mProblemAnswer.setVisibility(Component.INVISIBLE);//不显示
        }
        if (cProblem.isCollection()) {//收藏过
//            显示解析
            mProblemSave.setText("已收藏");
        } else {//没有收藏过
            mProblemSave.setText("收藏");
        }
        switch (cProblem.getProblemType()) {
            case 1://选择
                HiLog.info(SelectProblemSlice.LABEL_LOG, "清空图片了啦");
                for (int i = 0; i < 4; ++i) {// 清空图片
                    imageViewsCheck[i].setPixelMap(0);
                }
                problemSelect = (ProblemSelect) problem;

                HiLog.info(LABEL_LOG, "上一题下一题");
                mProblemUp.setVisibility(Component.HIDE);
                mTvTitle.setText(problemSelect.getTitle());
                mTvOptionA.setText(problemSelect.getOptionA());
                mTvOptionB.setText(problemSelect.getOptionB());
                mTvOptionC.setText(problemSelect.getOptionC());
                mTvOptionD.setText(problemSelect.getOptionD());
                String url = ServiceConfig.SERVICE_ROOT + "/img/";
                HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionApic()).into(mIvOptionA);
                HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionBpic()).into(mIvOptionB);
                HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionCpic()).into(mIvOptionC);
                HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionDpic()).into(mIvOptionD);
                break;
        }
    }


    //初始化
    private void initGson() {
//        初始化工作
        mTvUserC.setText(Constant.User.getUserCount() + "");
        mTvUserE.setText(Constant.User.getUserExperience() + "");
        cProblem = new Problem();
        okHttpClient = new OkHttpClient();
        myProblems = new ArrayList<>();
        gson = new Gson();

    }

    // 获取题目
    private void getProblemFromServer(int type) {
//        朝代跳转
        mProblemAnswer.setVisibility(Component.INVISIBLE);//隐藏解析按钮
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + ServiceConfig.GET_PROBLEMS + type + "/" + dynastyId + "")
                .build();
        Call call = okHttpClient.newCall(request);
        HiLog.info(LABEL_LOG, "获取题目路径" + ":" + ServiceConfig.SERVICE_ROOT + ServiceConfig.GET_PROBLEMS + type + "/" + dynastyId + "");
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失）
                HiLog.info(LABEL_LOG, "获取题目路径失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                成功
                String jsonData = response.body().string();
//                  得到题目 反序列化
                Problem problem = gson.fromJson(jsonData, Problem.class);
                HiLog.info(LABEL_LOG, "problem :" + problem.toString());
//                查看是否已经收藏
                checkIsUserCollection(problem.getProblemId());
//                修改当前题目
                cProblem = problem;
//                   根据题目类型解析
                String[] contents1 = problem.getProblemContent().split("&&&");
                switch (type) {
                    case 1:
                        problemSelect = new ProblemSelect();
                        problemSelect.setDynastyId(dynastyId);
                        problemSelect.setProblemType(1);
                        problemSelect.setProblemId(problem.getProblemId());
                        problemSelect.setTitle(contents1[0]);
                        problemSelect.setOptionA(contents1[1]);
                        problemSelect.setOptionApic(contents1[2]);
                        problemSelect.setOptionB(contents1[3]);
                        problemSelect.setOptionBpic(contents1[4]);
                        problemSelect.setOptionC(contents1[5]);
                        problemSelect.setOptionCpic(contents1[6]);
                        problemSelect.setOptionD(contents1[7]);
                        problemSelect.setOptionDpic(contents1[8]);
                        problemSelect.setProblemKey(problem.getProblemKey());
                        problemSelect.setProblemDetails(problem.getProblemDetails());
//
                        if (mProblemSave.getText().equals("已收藏")) {
                            HiLog.info(LABEL_LOG, "已收藏修改");
                            problemSelect.setCollection(true);
                        }
                        myProblems.add(problemSelect);
//                        主线程运行
                        getMainTaskDispatcher().syncDispatch(new Runnable() {
                            @Override
                            public void run() {
                                mProblemUp.setVisibility(Component.HIDE);
                                mTvTitle.setText(problemSelect.getTitle());
                                mTvOptionA.setText(problemSelect.getOptionA());
                                mTvOptionB.setText(problemSelect.getOptionB());
                                mTvOptionC.setText(problemSelect.getOptionC());
                                mTvOptionD.setText(problemSelect.getOptionD());
                                String url = ServiceConfig.SERVICE_ROOT + "/img/";
                                HiLog.info(LABEL_LOG, "访问的路径" + url + problemSelect.getOptionApic());
                                HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionApic()).into(mIvOptionA);
                                HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionBpic()).into(mIvOptionB);
                                HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionCpic()).into(mIvOptionC);
                                HmOSImageLoader.with(SelectProblemSlice.this).load(url + problemSelect.getOptionDpic()).into(mIvOptionD);
                            }
                        });
                        break;
                }
            }
        });
    }


    //  上传答题结果 判断朝代是否解锁
    public void UpProblemAnwer(int problemId, int result) {
        HiLog.info(LABEL_LOG, "p答案 result：" + result);
//备注：result中1，表示正确，2表示错误
        switch (result) {
            case 1://正确
                HiLog.info(LABEL_LOG, "回答正确喽！经验+20,积分+10\"");
                // 判断
                if (Integer.parseInt(dynastyId) == Constant.UnlockDynasty.size()) {
                    //TODO 弹窗提示
                    HiLog.info(LABEL_LOG, "回答正确喽！经验+20,积分+10\"");
//                    promptDialog.showInfo("回答正确喽！经验+20,积分+10");
                } else {//正确 未解锁
                    //TODO 弹窗提示
                    HiLog.info(LABEL_LOG, "回答正确喽！经验+20");
//                    promptDialog.showInfo("回答正确喽！经验+20");
                }
                break;
            case 2:
                //弹窗提示
                ToastUtil.showSickToast(SelectProblemSlice.this,"很遗憾~ 回答错误,经验+20");
                break;
        }
        if (before.equals("info")) {
            return;//收藏页不上传
        }
        String url = ServiceConfig.SERVICE_ROOT + "/problem/answer/" +
                Constant.User.getUserId() + "/" + dynastyId + "/" + problemId + "/" + result;
        HiLog.info(LABEL_LOG, "上传题目答案");
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                getMainTaskDispatcher().syncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        ProblemCheckAnswer problemCheckAnswer = gson.fromJson(result, ProblemCheckAnswer.class);
//                       加积分加经验
                        HiLog.info(LABEL_LOG, "答案 加积分加经验");
                        Constant.User.setUserExperience(problemCheckAnswer.getUserExperience());
                        Constant.User.setUserCount(problemCheckAnswer.getUserCount());
                        mTvUserC.setText(problemCheckAnswer.getUserCount() + "");
                        mTvUserE.setText(problemCheckAnswer.getUserExperience() + "");
                    }
                });
            }
        });


    }

    //   监听器
    private void setListener() {
        MyListener myListener = new MyListener();
        mDlXuanA.setClickedListener(myListener);
        mDlXuanB.setClickedListener(myListener);
        mDlXuanC.setClickedListener(myListener);
        mDlXuanD.setClickedListener(myListener);
        mProblemAnswer.setClickedListener(myListener);
        mIvCheckA.setClickedListener(myListener);
        mIvCheckB.setClickedListener(myListener);
        mIvCheckC.setClickedListener(myListener);
        mIvCheckD.setClickedListener(myListener);
        mProblemUp.setClickedListener(myListener);
        mProblemSave.setClickedListener(myListener);
        mProblemP.setClickedListener(myListener);
        mProblemN.setClickedListener(myListener);

    }

    private void findComponents() {
        mTvUserE = (Text) findComponentById(ResourceTable.Id_tv_user_e);
        mTvUserC = (Text) findComponentById(ResourceTable.Id_tv_user_c);
        mTvTitle = (Text) findComponentById(ResourceTable.Id_tv_title);
        mIvOptionA = (Image) findComponentById(ResourceTable.Id_iv_optionA);
        mTvOptionA = (Text) findComponentById(ResourceTable.Id_tv_optionA);
        mIvCheckA = (Image) findComponentById(ResourceTable.Id_iv_checkA);
        mDlXuanA = (DirectionalLayout) findComponentById(ResourceTable.Id_dl_xuan_a);
        mIvOptionB = (Image) findComponentById(ResourceTable.Id_iv_optionB);
        mTvOptionB = (Text) findComponentById(ResourceTable.Id_tv_optionB);
        mIvCheckB = (Image) findComponentById(ResourceTable.Id_iv_checkB);
        mDlXuanB = (DirectionalLayout) findComponentById(ResourceTable.Id_dl_xuan_b);
        mIvOptionC = (Image) findComponentById(ResourceTable.Id_iv_optionC);
        mTvOptionC = (Text) findComponentById(ResourceTable.Id_tv_optionC);
        mIvCheckC = (Image) findComponentById(ResourceTable.Id_iv_checkC);
        mDlXuanC = (DirectionalLayout) findComponentById(ResourceTable.Id_dl_xuan_c);
        mIvOptionD = (Image) findComponentById(ResourceTable.Id_iv_optionD);
        mTvOptionD = (Text) findComponentById(ResourceTable.Id_tv_optionD);
        mIvCheckD = (Image) findComponentById(ResourceTable.Id_iv_checkD);
        mDlXuanD = (DirectionalLayout) findComponentById(ResourceTable.Id_dl_xuan_d);
        mProblemAnswer = (Text) findComponentById(ResourceTable.Id_problem_answer);
        mProblemSave = (Text) findComponentById(ResourceTable.Id_problem_save);
        mProblemUp = (Text) findComponentById(ResourceTable.Id_problem_up);
        mProblemP = (Text) findComponentById(ResourceTable.Id_problem_p);
        mProblemN = (Text) findComponentById(ResourceTable.Id_problem_n);
        imageViewsCheck = new Image[]{mIvCheckA, mIvCheckB, mIvCheckC, mIvCheckD};


    }

    //    监听器类
    class MyListener implements Component.ClickedListener {
        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
                case ResourceTable.Id_dl_xuan_a:
                    //                判断正误的方法
                    boolean b = checkUserXuanAnswer(problemSelect.getOptionA());
                    if (b) {
                        imageViewsCheck[0].setPixelMap(ResourceTable.Media_p_yes);
                    } else {
                        //显示错误 正确显示正确
                        getCheckImage(0);
                    }
                    break;
                case ResourceTable.Id_dl_xuan_b:
                    boolean b2 = checkUserXuanAnswer(problemSelect.getOptionB());
                    if (b2) {
                        imageViewsCheck[1].setImageAndDecodeBounds(ResourceTable.Media_p_yes);
                    } else {
                        //显示错误 正确显示正确
                        getCheckImage(1);
                    }
                    break;
                case ResourceTable.Id_dl_xuan_c:
                    boolean b3 = checkUserXuanAnswer(problemSelect.getOptionC());
                    if (b3) {
                        imageViewsCheck[2].setImageAndDecodeBounds(ResourceTable.Media_p_yes);
                    } else {
                        //显示错误 正确显示正确
                        getCheckImage(2);
                    }
                    break;
                case ResourceTable.Id_dl_xuan_d:
                    boolean b4 = checkUserXuanAnswer(problemSelect.getOptionD());
                    if (b4) {
                        imageViewsCheck[3].setImageAndDecodeBounds(ResourceTable.Media_p_yes);
                    } else {
                        //显示错误 正确显示正确
                        getCheckImage(3);
                    }
                    break;
                case ResourceTable.Id_problem_answer://解析
                    getProblemAnswer();
                    break;
                case ResourceTable.Id_problem_save://收藏
                    HiLog.info(LABEL_LOG, "当先题目: " + cProblem.toString());
                    String s = mProblemSave.getText();
                    if (s.equals("收藏")) {//取消收藏
                        String urlSaveProblem = ServiceConfig.SERVICE_ROOT + "/userproblem/collect/" +
                                Constant.User.getUserId() + "/" + dynastyId + "/" + cProblem.getProblemId() + "";
                        HiLog.info(LABEL_LOG, "urlSaveProblem:  " + urlSaveProblem);
                        Request.Builder builder = new Request.Builder();
                        builder.url(urlSaveProblem);
                        //构造请求类
                        Request request = builder.build();
                        final Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                HiLog.info(LABEL_LOG, "onFailure: " + "收藏失败");
                                // 提示
                                ToastUtil.showSickToast(SelectProblemSlice.this,"网络拥堵，请稍后试试喔");
//                                promptDialog.showError("网络较差，请稍后");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
//                                eventHandler.sendEvent(4);
//                                修改界面
                                getMainTaskDispatcher().syncDispatch(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProblemSave.setText("已收藏");
                                        ToastUtil.showEncourageToast(SelectProblemSlice.this,"收藏成功啦");

                                        if (before.equals("types"))
                                            myProblems.get(cIndex).setCollection(true);
                                    }
                                });

                            }
                        });
                    } else {
//                    /userproblem/uncollect/{userId}/{dynastyId}/{problemId}
                        String urlSaveProblem = ServiceConfig.SERVICE_ROOT + "/userproblem/uncollect/" +
                                Constant.User.getUserId() + "/" + dynastyId + "/" + cProblem.getProblemId() + "";
                        HiLog.info(LABEL_LOG, "取消收藏路径" + urlSaveProblem);
                        Request.Builder builder = new Request.Builder();
                        builder.url(urlSaveProblem);
                        //构造请求类
                        Request request = builder.build();
                        final Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
//                                promptDialog.showError("网络较差，请稍后");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
//                                eventHandler.sendEvent(5);
                                getMainTaskDispatcher().syncDispatch(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProblemSave.setText("收藏");
                                        ToastUtil.showEncourageToast(SelectProblemSlice.this,"取消收藏成功啦");

                                        if (before.equals("types"))
                                            myProblems.get(cIndex).setCollection(false);
                                    }
                                });

                            }
                        });
                    }
                    break;
                case ResourceTable.Id_problem_p://上一题
                    doResult(RIGHT);
                    break;
                case ResourceTable.Id_problem_n://下一题
                    doResult(LEFT);
                    break;
            }
        }
    }


    // 弹窗提示解析
    private void getProblemAnswer() {
        CommonDialog commonDialog = new CommonDialog(this);
        Component dialog = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_dialog_problem_answer, null, false);
        Text text = (Text) dialog.findComponentById(ResourceTable.Id_tv_answer);
        Button btn = (Button) dialog.findComponentById(ResourceTable.Id_btn_close);
        text.setText(cProblem.getProblemDetails());
        btn.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                commonDialog.hide();
            }
        });
        commonDialog.setSize(1000, 600);
        commonDialog.setContentCustomComponent(dialog);
        commonDialog.show();

    }


    //  查看是否已经收藏该题目
    private void checkIsUserCollection(int problemId) {
        String url = ServiceConfig.SERVICE_ROOT + "/userproblem/iscollection/" + Constant.User.getUserId() + "/" + dynastyId + "/" + problemId;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                HiLog.info(LABEL_LOG, "获取是否收藏题目失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("true")) {
                    getMainTaskDispatcher().syncDispatch(new Runnable() {
                        @Override
                        public void run() {
                            mProblemSave.setText("已收藏");
                        }
                    });
                }

            }
        });


    }

    //    检查是否答过
    private boolean checkUserXuanAnswer(String option) {
        if (before.equals("types")) {//朝代跳转
            myProblems.get(cIndex).setGetAnswer(true);
        }
        mProblemAnswer.setVisibility(Component.VISIBLE);//显示解析按钮
        if (isGetAnswer) {//isGetAnswer为true 答过   不会上传
            return false;
        }
        if (problemSelect.getProblemKey().equals(option) && !isGetAnswer) {
//        上传结果
            UpProblemAnwer(problemSelect.getProblemId(), 1);
            isGetAnswer = true;
            return true;
        } else {
            UpProblemAnwer(problemSelect.getProblemId(), 2);
            isGetAnswer = true;
            return false;
        }

    }

    //    更换选择题 正确与否的图片
    private void getCheckImage(int index) {
        imageViewsCheck[index].setPixelMap(ResourceTable.Media_p_no);

        if (problemSelect.getProblemKey().equals(problemSelect.getOptionA())) {
            imageViewsCheck[0].setPixelMap(ResourceTable.Media_p_yes);
        }
        if (problemSelect.getProblemKey().equals(problemSelect.getOptionB())) {
            imageViewsCheck[1].setPixelMap(ResourceTable.Media_p_yes);
        }
        if (problemSelect.getProblemKey().equals(problemSelect.getOptionC())) {
            imageViewsCheck[2].setPixelMap(ResourceTable.Media_p_yes);
        }
        if (problemSelect.getProblemKey().equals(problemSelect.getOptionD())) {
            imageViewsCheck[3].setPixelMap(ResourceTable.Media_p_yes);
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }


}
