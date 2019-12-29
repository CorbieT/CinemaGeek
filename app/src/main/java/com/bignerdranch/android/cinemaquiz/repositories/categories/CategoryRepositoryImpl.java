package com.bignerdranch.android.cinemaquiz.repositories.categories;

import android.content.res.XmlResourceParser;

import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.common.XmlPullParserHelper;
import com.bignerdranch.android.cinemaquiz.model.Category;

import java.util.List;

import io.reactivex.Single;

public class CategoryRepositoryImpl implements CategoryRepository<Category> {

    private XmlPullParserHelper xmlPullParserHelper;

    public CategoryRepositoryImpl(XmlResourceParser parser,
                                  SharedPrefHelper sharedPrefHelper) {
        this.xmlPullParserHelper = new XmlPullParserHelper(parser, sharedPrefHelper);
    }

    @Override
    public Single<List<Category>> parseCategories() {
            return xmlPullParserHelper.getCategoriesFromXml();
    }
}
