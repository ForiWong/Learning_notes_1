https://www.cnblogs.com/andy-songwei/p/10955062.html

## 自定义View的三个方法
自定义View的时候一般需要重写父类的onMeasure()、onLayout()、onDraw()三个方法，来完成视图的展示过程。

一个完整的绘制流程包括measure、layout、draw三个步骤，其中：
measure：测量。系统会先根据xml布局文件和代码中对控件属性的设置，来获取或者计算出每个View和ViewGrop的
尺寸，并将这些尺寸保存下来。
layout：布局。根据测量出的结果以及对应的参数，来确定每一个控件应该显示的位置。
draw：绘制。确定好位置后，就将这些控件绘制到屏幕上。

## MeasureSpec 测量规范
从这段代码中，咱们可以得到如下的信息：
1）MeasureSpec概括了从父布局传递给子view布局要求。每一个MeasureSpec代表了宽度或者高度要求，它由size
（尺寸）和mode（模式）组成。
2）有三种可能的mode：UNSPECIFIED、EXACTLY、AT_MOST
3）UNSPECIFIED：未指定尺寸模式。父布局没有对子view强加任何限制。它可以是任意想要的尺寸。
（笔者注：这个在工作中极少碰到，据说一般在系统中才会用到，后续会讲得很少）
4）EXACTLY：精确值模式。父布局决定了子view的准确尺寸。子view无论想设置多大的值，都将限定在那个边界内。
（笔者注：也就是layout_width属性和layout_height属性为具体的数值，如50dp，或者设置为match_parent，设置
为match_parent时也就明确为和父布局有同样的尺寸，所以这里不要以为笔者搞错了。当明确为精确的尺寸后，其
也就被给定了一个精确的边界）
5）AT_MOST：最大值模式。子view可以一直大到指定的值。（笔者注：也就是其宽高属性设置为wrap_content，那
么它的最大值也不会超过父布局给定的值，所以称为最大值模式）
6）MeasureSpec被实现为int型来减少对象分配。该类用于将size和mode元组装包和拆包到int中。（笔者注：也就
是将size和mode组合或者拆分为int型数据）
7）分析代码可知，一个MeasureSpec的模式如下所示，int长度为32位置，高2位表示mode，后30位用于表示size

## ViewGroup.LayoutParams 布局参数
1）LayoutParams被view用于告诉它们的父布局它们想要怎样被布局。（笔者注：字面意思就是布局参数）
2）该LayoutParams基类仅仅描述了view希望宽高有多大。对于每一个宽或者高，可以指定为以下三种值中的一个：
MATCH_PARENT,WRAP_CONTENT,an exact number。（笔者注：FILL_PARENT从API8开始已经被MATCH_PARENT取代了，
所以下文就只提MATCH_PARENT）
3）MATCH_PARENT：意味着该view希望和父布局尺寸一样大，如果父布局有padding，则要减去该padding值。
4）WRAP_CONTENT：意味着该view希望其大小为仅仅足够包裹住其内容即可，如果自己有padding，则要加上该padding值。
5）对ViewGroup不同的子类，也有相应的LayoutParams子类。
6）其width和height属性对应着layout_width和layout_height属性。

## View.measure()方法 测量过程
final型的，View子类都不能重写该方法。
//参数widthMeasureSpec：父布局加入的水平空间要求；
//参数heightMeasureSpec：父布局加入的垂直空间要求。

public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
......
// measure ourselves, this should set the measured dimension flag back
onMeasure(widthMeasureSpec, heightMeasureSpec);
......
}

一个view的实际测量工作是在被本方法所调用的onMeasure(int，int)方法中实现的。所以，只有onMeasure(int,int)
可以并且必须被子类重写（笔者注：这里应该指的是，ViewGroup的子类必须重写该方法，才能绘制该容器内的子view。
如果是自定义一个子控件，extends View，那么并不是必须重写该方法）；
系统将其定义为一个final方法，可见系统不希望整个测量流程框架被修改。

## View.onMeasure()方法
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
}
//这个只是默认的实现。一般子类还是要自己实现，并调用setMeasuredDimension()

当重写该方法时，您必须调用setMeasuredDimension(int,int)来存储该view测量出的宽和高。如果不这样做将会触
发IllegalStateException，由measure(int,int)抛出。调用基类的onMeasure(int,int)方法是一个有效的方法。
测量的基类实现默认为背景的尺寸，除非更大的尺寸被MeasureSpec所允许。子类应该重写onMeasure(int,int)方法
来提供对内容更好的测量。

如果该方法被重写，子类负责确保测量的高和宽至少是该view的mininum高度和mininum宽度值（链接getSuggested
MininumHeight()和getSuggestedMininumWidth()）；
最后通过从根View到叶子节点的遍历和递归，最终还是会在叶子view中调用setMeasuredDimension(...)来实现最终
的测量。

## View.setMeasuredDimension()方法
存储View测量出来的宽和高。

ViewGroup 没有重写onMeasure()方法
但是提供了测量子类的方法：measureChild()方法和measureChildWithMargins()方法
比如，LinearLayout是ViewGroup的子类，是实现了onMeasure()的，并在其中调用了setMeasuredDimension。
容器类先遍历完成叶子View的Measure，再完成本容器的Measure。

## DecorView测量的大致流程
DecorView的继承链：DecorView extends FrameLayout extends ViewGroup extends View。所以在这个继承过程中
一定会有子类重写onMeasure方法，当DecorView第一次调用到measure()方法后，流程就开始切换到重写的onMeasure
()中了。

//=============DecorView.java=============
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
......
super.onMeasure(widthMeasureSpec, heightMeasureSpec);
......
}

//=============FrameLayout.java=============
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
int count = getChildCount();
for (int i = 0; i < count; i++) {
final View child = getChildAt(i);
......
measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
......
}
......
setMeasuredDimension(......)
...... }

从上述FrameLayout中重写的onMeasure方法中可以看到，是先把子view测量完成后，最后才去调用setMeasuredDimension(...)
来测量自己的。事实上，整个测量过程就是从子view开始测量，然后一层层往上再测量父布局，直到DecorView为止的。


## layout布局
当measure过程完成后，接下来就会进行layout阶段，即布局阶段。在前面measure的作用是测量每个view的尺寸，
而layout的作用是根据前面测量的尺寸以及设置的其它属性值，共同来确定View的位置。

由于DecorView是一个容器，是ViewGroup子类，所以跟踪代码的时候，实际上是先进入到ViewGroup类中的layout方法中。

//ViewGroup.java
public final void layout(int l, int t, int r, int b)

这是一个final类型的方法，所以自定义 的ViewGroup子类无法重写该方法，可见系统不希望自定义的ViewGroup子类
破坏layout流程。继续追踪super.layout方法，又跳转到了View中的layout方法。

//View
public void layout(int l, int t, int r, int b)
1）给view和它的所有后代分配尺寸和位置。
2）这是布局机制的第二个阶段（第一个阶段是测量）。在这一阶段中，每一个父布局都会对它的子view进行布局来
放置它们。一般来说，该过程会使用在测量阶段存储的child测量值。
3）派生类不应该重写该方法。有子view的派生类（笔者注：也就是容器类，父布局）应该重写onLayout方法。在重
写的onLayout方法中，它们应该为每一子view调用layout方法进行布局。
4）参数依次为：Left、Top、Right、Bottom四个点相对父布局的位置。

## onLayout方法让父布局调用对子view的布局
//View
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {...}

1）当该view要分配尺寸和位置给它的每一个子view时，该方法会从layout方法中被调用。
2）有子view的派生类（笔者注：也就是容器，父布局）应该重写该方法并且为每一个子view调用layout。

我们发现这是一个空方法，因为layout过程是父布局容器布局子view的过程，onLayout方法叶子view没有意义，只
有ViewGroup才有用。所以，如果当前View是一个容器，那么流程会切到被重写的onLayout方法中。

//ViewGroup.java
@Override
protected abstract void onLayout(boolean changed, int l, int t, int r, int b);

进入到ViewGroup类中发现，该方法被定义为了abstract方法，所以以后凡是直接继承自ViewGroup类的容器，就必须要重写onLayout方法。

## DecerView继承自FrameLayout，咱们继续到FrameLayout类中重写的onLayout方法看看。
//FrameLayout.java
@Override
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
layoutChildren(left, top, right, bottom, false /* no force left gravity */);
}

void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
    final int count = getChildCount();
    ......
    for (int i = 0; i < count; i++) {
         final View child = getChildAt(i);
         if (child.getVisibility() != GONE) {
             final LayoutParams lp = (LayoutParams) child.getLayoutParams();

             final int width = child.getMeasuredWidth();
             final int height = child.getMeasuredHeight();
             ......
             child.layout(childLeft, childTop, childLeft + width, childTop + height);
        }
}

这里仅贴出关键流程的代码，这里面也是对每一个child调用layout方法的。如果该child仍然是父布局，会继续递归下去 ；
如果是叶子view，则会走到view的onLayout空方法，该叶子view布局流程走完。另外，我们看到第15行和第16行中，width和
height分别来源于measure阶段存储的测量值，如果这里通过其它渠道赋给width和height值，那么measure阶段就不需要了，
这也就是我前面提到的，onLayout是必需要实现的（不仅会报错，更重要的是不对子view布局的话，这些view就不会显示了），
而measure过程可以不要。


## View类的draw方法
当layout完成后，就进入到draw阶段了，在这个阶段，会根据layout中确定的各个view的位置将它们画出来。

//View.java
/**
* Manually render this view (and all of its children) to the given Canvas.
* The view must have already done a full layout before this function is
* called.  When implementing a view, implement
* {@link #onDraw(android.graphics.Canvas)} instead of overriding this method.
* If you do need to override this method, call the superclass version.
*
* @param canvas The Canvas to which the View is rendered.
*/
@CallSuper
public void draw(Canvas canvas) {
......
/*
* Draw traversal performs several drawing steps which must be executed
* in the appropriate order:
*
*      1. Draw the background
*      2. If necessary, save the canvas' layers to prepare for fading
*      3. Draw view's content
*      4. Draw children
*      5. If necessary, draw the fading edges and restore layers
*      6. Draw decorations (scrollbars for instance)
*/

        // Step 1, draw the background, if needed
        int saveCount;

        if (!dirtyOpaque) {
            drawBackground(canvas);
        }

        // skip step 2 & 5 if possible (common case)
        ......
        // Step 3, draw the content
        if (!dirtyOpaque) onDraw(canvas);

        // Step 4, draw the children
        dispatchDraw(canvas);
        ......
        // Step 6, draw decorations (foreground, scrollbars)
        onDrawForeground(canvas);45         ......
    }

从代码上看，这里做了很多工作，咱们简单说明一下，有助于理解这个“画”工作。
1）第一步：画背景。对应我我们在xml布局文件中设置的“android:background”属性，这是整个“画”过程的第
一步，这一步是不重点，知道这里干了什么就行。

2）第二步：画内容（第2步和第5步只有有需要的时候才用到，这里就跳过）。比如TextView的文字等，这是重点，
onDraw方法，后面详细介绍。

3）第三步：画子view。dispatchDraw方法用于帮助ViewGroup来递归画它的子view。这也是重点，后面也要详细讲到。

4）第四步：画装饰。这里指画滚动条和前景。其实平时的每一个view都有滚动条，只是没有显示而已。同样这也不是
重点，知道做了这些事就行。

//=================View.java===============
/**
* Implement this to do your drawing.
*
* @param canvas the canvas on which the background will be drawn
*/
protected void onDraw(Canvas canvas) {
}

注释中说：实现该方法来做“画”工作。也就是说，具体的view需要重写该方法，来画自己想展示的东西，如文字，线条等。
DecorView中重写了该方法，所以流程会走到DecorView中重写的onDraw方法。

在源码中发现，像平时常用的LinearLayout、FrameLayout、RelativeLayout等常用的布局控件，都没有再重写该方法，
DecorView中也一样，而是只在ViewGroup中实现了dispatchDraw方法的重写。所以当DecorView执行完onDraw方法后，
流程就会切到ViewGroup中的dispatchDraw方法了。

//=============ViewGroup.java============
@Override
protected void dispatchDraw(Canvas canvas) {
final int childrenCount = mChildrenCount;
final View[] children = mChildren;
......
for (int i = 0; i < childrenCount; i++) {
more |= drawChild(canvas, child, drawingTime);
......
}
......
}
