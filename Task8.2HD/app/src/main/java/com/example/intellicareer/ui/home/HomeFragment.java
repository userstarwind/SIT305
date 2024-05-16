package com.example.intellicareer.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intellicareer.DBHelper;
import com.example.intellicareer.HomeActivity;
import com.example.intellicareer.MyApplication;
import com.example.intellicareer.R;
import com.zhpan.bannerview.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private BannerViewPager<CustomBean> mViewPager;
    private List<CustomBean> customBeanList;
    private List<News> newsList;
    private int[] newsImageIds = {R.drawable.news_image_1, R.drawable.news_image_2, R.drawable.news_image_3, R.drawable.news_image_4};
    private String[] newsTitles = {"Tech Giant Introduces New Remote Work Policy, Boosting Employee Satisfaction",
            "Green Office Spaces Become Workplace Trend, Embracing Sustainability",
            "Workplace Health in Focus as Company Launches Fitness Program",
            "AI Training Courses Gain Popularity as Companies Accelerate Digital Transformation"};
    private String[] newsContents = {"Today, renowned tech company Tech Innovators announced a new remote work policy that allows employees to work from home three days a week. This initiative aims to enhance employee flexibility and satisfaction. Following the implementation of the new policy, the company has seen significant improvements in productivity and overall employee happiness. One employee remarked, \"This policy not only gives us more time with our families but also helps us better balance work and life.\"",
            "With increasing awareness of environmental issues, more companies are adopting green office spaces. Recently, renowned advertising firm Green Creative announced that their new office building is constructed entirely from eco-friendly materials and equipped with advanced energy-saving systems. The company stated that this not only helps reduce their carbon footprint but also provides a healthier work environment. Employees reported feeling more comfortable and healthier working in such an environment.",
            "Recently, a large financial institution announced a comprehensive employee fitness program aimed at improving health and productivity. The program includes free gym memberships, weekly yoga classes, and healthy eating guides. The management emphasized that healthy employees are the company's greatest asset, and their well-being is crucial for sustainable growth. Participants of the program expressed appreciation for the company's care, stating that it motivates them to contribute more to the company."
            , "In the wave of digital transformation, more companies are focusing on upskilling their employees. Recently, a well-known manufacturing company launched a series of AI training courses to enhance employees' technical skills and drive digital transformation. The company stated that these courses would enable employees to master the latest AI technologies and apply them effectively in their work. Employees who attended the training reported that the courses were rich in content and significantly improved their professional skills."};
    private String[] newsDates = {"2024/5/11", "2024/5/12", "2024/5/13", "2024/5/12"};
    private MyApplication myApplication;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customBeanList = new ArrayList<>();
        customBeanList.add(new CustomBean(R.drawable.carousel_img1, "Connecting Talent with Opportunity"));
        customBeanList.add(new CustomBean(R.drawable.carousel_img2, "Stay Ahead of the Curve"));
        customBeanList.add(new CustomBean(R.drawable.carousel_img3, "Lead the Way in Your Career"));
        myApplication = (MyApplication) getActivity().getApplication();
        newsList = new ArrayList<>();
        for (int i = 0; i < newsImageIds.length; i++) {
            newsList.add(new News(newsTitles[i], newsImageIds[i], newsContents[i], newsDates[i]));
        }
        newsRecyclerView = view.findViewById(R.id.home_recycler_view);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsRecyclerView.setAdapter(new NewsAdapter(newsList, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News news) {
                myApplication.setSelectedNews(news);
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_home);
                navController.navigate(R.id.action_homeFragment_to_newsDetailsFragment);
            }
        }));
        mViewPager = view.findViewById(R.id.home_banner_view);
        mViewPager.setIndicatorVisibility(View.GONE);
        mViewPager.setAdapter(new
                BannerAdapter());
        mViewPager.setLifecycleRegistry(
                getLifecycle());
        mViewPager.create(customBeanList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}