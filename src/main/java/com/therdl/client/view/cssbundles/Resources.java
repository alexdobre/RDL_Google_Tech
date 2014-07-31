package com.therdl.client.view.cssbundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


/**
 * ClientBundle type with deferred binding allows tools for compression and efficient injection of
 * resources like css, images and javascript tools/widgets
 * see http://www.gwtproject.org/doc/latest/DevGuideClientBundle.html
 *
 * @CssResource.NotStrict this annotation is necessary when a css file breaks Googles restrictive css
 * compilers heuristics, code that breaks compilers heuristics will cause the GWT compiler to fail
 */
public interface Resources extends ClientBundle {
	//Is necessary instantiates the Resources using GWT.create(Class) technique is used
	// under the hood to instruct the compiler to use deferred binding.
	public static final Resources INSTANCE = GWT.create(Resources.class);

	@Source("imagebund/RDL_logo_med_no_text.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource landingImage();

	@Source("imagebund/FastCap.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource FastCapImage();

	@Source("imagebund/Habit.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource HabitImage();

	@Source("imagebund/Material.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource MaterialImage();

	@Source("imagebund/Pledge.jpg")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource PledgeImage();
  /*
    @Source("imagebund/Tag.jpg")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource TagImage();        */

	@Source("imagebund/Post.jpg")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource PostImage();

	@Source("imagebund/Proposal.jpg")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource ProposalImage();

	@Source("imagebund/Snip.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource SnipImage();


	@Source("imagebund/Stream.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource StreamImageGif();

	@Source("imagebund/Tag.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource TagImageGif();

	@Source("imagebund/Thread.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource ThreadImageGif();

	@Source("imagebund/Post.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource PostImageGif();

	@Source("imagebund/Proposal.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource ProposalImageGif();

	@Source("imagebund/Pledge.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource PledgeImageGif();

	@Source("imagebund/Stream.jpg")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource StreamImage();

	@Source("imagebund/facebook.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource facebookImage();

	@Source("imagebund/twitter.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource twitterImage();

	@Source("imagebund/reddit.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource redditImage();

	@Source("imagebund/youtube.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource youtubeImage();

	@Source("imagebund/loader.gif")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource loaderImage();

	@Source("imagebund/arrow-down-green1.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource arrowDownGreen();

	@Source("imagebund/arrow-up-green1.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource arrowUpGreen();

	@Source("imagebund/arrow-down-grey.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource arrowDownGrey();

	@Source("imagebund/arrow-up-grey.png")
	@ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
	ImageResource arrowUpGrey();
}
