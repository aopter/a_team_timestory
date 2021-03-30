package com.example.timestory.ability.problem.slice;


import com.example.timestory.ResourceTable;
import com.example.timestory.ability.problem.adapter.ProblemSavedItemProvider;
import com.example.timestory.ability.problem.adapter.ProblemleftDynastyItemProvider;
import com.example.timestory.constant.Constant;
import com.example.timestory.constant.ServiceConfig;
import com.example.timestory.entity.Problem;
import com.example.timestory.entity.UserUnlockDynasty;
import com.example.timestory.entity.problem.PageCount;
import com.example.timestory.entity.problem.ProblemCheckAnswer;
import com.example.timestory.entity.problem.ProblemCollection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.hiviewdfx.HiLog;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProblemCollectionActivitySlice extends AbilitySlice {
    private ListContainer mTypeDynasty;//所有朝代
    private Text mTvCollectionNull;//空收藏显示
    private ListContainer mReProblems;//所有的题目
    private List<ProblemCollection> problemCollections;//题目列表
    private List<Problem> problems;//题目列表
    private OkHttpClient okHttpClient;
    private int cPageCount;//当前页
    private Gson gson;
    private String pageCount = "1";//分页数
    private int pageSize = 1;//容量
    private String cDynastyId = "0";//当前朝代


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_problem_collection_activity);
        findComponents();
        okHttpClient = new OkHttpClient();
        problems = new ArrayList<>();
        problemCollections = new ArrayList<>();
        cPageCount = 1;
        gson = new Gson();
        mTvCollectionNull.setVisibility(Component.HIDE);
        initDynastys();//初始化左边  所有朝代
        initProblems();//获得用户收藏的题目=======

    }

    //    初始化路径
    private void initProblems() {
        cPageCount = 1;
        String url = ServiceConfig.SERVICE_ROOT + "/userproblem/count/" + Constant.User.getUserId() + "/" + pageSize + "";
        if (!cDynastyId.equals("0")) {
            url = ServiceConfig.SERVICE_ROOT + "/userproblem/count/" + Constant.User.getUserId() + "/" + cDynastyId + "/" + pageSize + "";
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                失败
                HiLog.info(SelectProblemSlice.LABEL_LOG, "获取分页数失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
//成功
                String data = response.body().string();
                PageCount page = gson.fromJson(data, PageCount.class);
                pageCount = page.getCount();//分页数
                if (pageCount.equals("0")) {//没有题目
//                   提示没有收藏题目 快点收藏吧
                    getMainTaskDispatcher().syncDispatch(new Runnable() {
                        @Override
                        public void run() {
                            HiLog.info(SelectProblemSlice.LABEL_LOG, "用户没有收藏任何题目");
                            mTvCollectionNull.setVisibility(Component.VISIBLE);
                            mReProblems.setVisibility(Component.HIDE);
                        }
                    });
                    return;
                }
//                一页三个的分页数 现在想要一页全部显示完 则是 新分页数
                pageSize = pageSize * Integer.parseInt(pageCount);
                getProblems();
            }
        });
    }

    //    获取题目本身
    private void getProblems() {
        //        获取题目
        String pUrl = ServiceConfig.SERVICE_ROOT + "/userproblem/search/" + Constant.User.getUserId() + "/" + cPageCount + "/" + pageSize + "";
        if (!cDynastyId.equals("0")) {//按朝代分
            pUrl = ServiceConfig.SERVICE_ROOT + "/userproblem/search/" + Constant.User.getUserId() + "/" + cDynastyId + "/" + cPageCount + "/" + pageSize + "";
        }

        HiLog.info(SelectProblemSlice.LABEL_LOG, "请求收藏题目路径" + pUrl);
        Request.Builder builder2 = new Request.Builder();
        builder2.url(pUrl);
        //构造请求类
        Request request2 = builder2.build();
        final Call call2 = okHttpClient.newCall(request2);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HiLog.info(SelectProblemSlice.LABEL_LOG, "获取题目失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String problemJson = response.body().string();
                problemCollections.clear();//清空
                problems.clear();
                problemCollections = gson.fromJson(problemJson, new TypeToken<List<ProblemCollection>>() {
                }.getType());
                if (problemCollections.size() <= 0) {
                    // 提示没有收藏题目 快点收藏吧
                    getMainTaskDispatcher().syncDispatch(new Runnable() {
                        @Override
                        public void run() {
                            HiLog.info(SelectProblemSlice.LABEL_LOG, "用户没有收藏任何题目");
                            mTvCollectionNull.setVisibility(Component.VISIBLE);
                            mReProblems.setVisibility(Component.HIDE);
                        }
                    });

                    return;
                } else {
                    // 有题目   显示
                    getMainTaskDispatcher().syncDispatch(new Runnable() {
                        @Override
                        public void run() {
                            HiLog.info(SelectProblemSlice.LABEL_LOG, "用户收藏了题目" + problemCollections.size() + "道");
                            mTvCollectionNull.setVisibility(Component.HIDE);
                            mReProblems.setVisibility(Component.VISIBLE);
                        }
                    });
                }
                for (int i = 0; i < problemCollections.size(); ++i) {
                    //设置朝代号
                    problemCollections.get(i).getProblem().setDynastyId(problemCollections.get(i).getDynasty().getDynastyId() + "");
                    if (problemCollections.get(i).getProblem().getProblemType() == 1){
                        problems.add(problemCollections.get(i).getProblem());
                    }
                }
//                获取题目并且填充布局
                getMainTaskDispatcher().syncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        HiLog.info(SelectProblemSlice.LABEL_LOG, "题目个数：" + problems.size());
                        ProblemSavedItemProvider problemSavedItemProvider = new ProblemSavedItemProvider(ProblemCollectionActivitySlice.this, ProblemCollectionActivitySlice.this.getContext(), problems);
                        mReProblems.setItemProvider(problemSavedItemProvider);
                    }
                });
            }
        });

    }

    //    初始化所有朝代
    private void initDynastys() {
//       造假数据
        if (Constant.UnlockDynasty.size() <= 0) {
            for (int i = 0; i < 3; i++) {
                UserUnlockDynasty dynasty = new UserUnlockDynasty();
                dynasty.setDynastyName("唐朝" + i);
                dynasty.setDynastyId("11");
                Constant.UnlockDynasty.add(dynasty);
            }
        }
        List<String> dynastyName = new ArrayList<>();
        HiLog.info(SelectProblemSlice.LABEL_LOG, "朝代的长度" + ":" + Constant.UnlockDynasty.size() + "");

        for (int i = 0; i < Constant.UnlockDynasty.size(); i++) {
            dynastyName.add(Constant.UnlockDynasty.get(i).getDynastyName());
        }

//        加载到布局中
        ProblemleftDynastyItemProvider problemleftDynastyItemProvider = new ProblemleftDynastyItemProvider(this, this.getContext(), dynastyName);
        mTypeDynasty.setItemProvider(problemleftDynastyItemProvider);
//        每个朝代的点击事件 更换新的数据源
        mTypeDynasty.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                HiLog.info(SelectProblemSlice.LABEL_LOG, "你点击的是第" + ":" + i + "号");
                //                更换新的数据源
                changProblems(i);
            }
        });
    }

    //   根据不同的朝代 更换新的数据源
    private void changProblems(int index) {
        cPageCount = 1;
        pageSize = 3;
//      解锁的朝代
        cDynastyId = Constant.UnlockDynasty.get(index).getDynastyId();
        HiLog.info(SelectProblemSlice.LABEL_LOG, "你点击的dynastyId是" + ":" + cDynastyId);
//      加载题目
        initProblems();

    }


    private void findComponents() {
        mTypeDynasty = (ListContainer) findComponentById(ResourceTable.Id_type_dynasty);
        mTvCollectionNull = (Text) findComponentById(ResourceTable.Id_tv_collection_null);
        mReProblems = (ListContainer) findComponentById(ResourceTable.Id_re_problems);
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
