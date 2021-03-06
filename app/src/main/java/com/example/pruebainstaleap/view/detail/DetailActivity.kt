package com.example.pruebainstaleap.view.detail

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pruebainstaleap.R
import com.example.pruebainstaleap.db.model.ResultService
import com.example.pruebainstaleap.utils.BASE_URL_IMAGE
import com.example.pruebainstaleap.utils.RESULT_SERVICE
import com.example.pruebainstaleap.utils.database
import com.example.pruebainstaleap.utils.setImageAddOrRemoveMyList
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeController
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.imagepipeline.request.Postprocessor
import jp.wasabeef.fresco.processors.BlurPostprocessor
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var resultService: ResultService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        resultService = intent.getSerializableExtra(RESULT_SERVICE) as ResultService
        if (resultService != null) {
            val postprocessor: Postprocessor = BlurPostprocessor(this, 50)
            val imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(BASE_URL_IMAGE + resultService?.poster_path))
                    .setPostprocessor(postprocessor)
                    .build()
            val controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(ivBackgroundDetail.controller)
                .build() as PipelineDraweeController
            ivBackgroundDetail.controller = controller

            ivImageDetail.setImageURI(Uri.parse(BASE_URL_IMAGE + resultService?.poster_path))
            tvDescriptionDetail.text = resultService?.overview

            addOrRemoveMyList(
                ivAdd,
                resultService,
                false,
                R.drawable.ic_add,
                R.drawable.ic_check,
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivAdd -> addOrRemoveMyList(
                ivAdd,
                resultService,
                true,
                R.drawable.ic_check,
                R.drawable.ic_add
            )
        }
    }

    private fun addOrRemoveMyList(
        ivAdd: ImageView,
        resultService: ResultService?,
        isSetRoom: Boolean,
        image1: Int,
        image2: Int
    ) {
        val resultServiceDao = database?.resultServiceDao()
        lifecycleScope.launch {
            val isResultService = setImageAddOrRemoveMyList(
                resultServiceDao?.getResultServiceById(resultService?.id!!),
                ivAdd,
                resources,
                image1,
                image2
            )
            if (isSetRoom)
                if (isResultService) resultServiceDao?.setResultService(resultService!!)
                else resultServiceDao?.deleteResultServiceById(resultService?.id!!)
        }
    }
}