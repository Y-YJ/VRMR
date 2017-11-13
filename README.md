本指南适用于那些过去构建应用程序有基础知识，现在想知道构建强大的生产质量应用程序最佳实践和建议的体系结构的开发人员。
>注意：本指南假设读者熟悉Android框架。如果您不熟悉应用程序开发，请查看入门培训系列，其中包含本指南的必备主题。

### APP开发者面临的常见问题
与传统的桌面应用程序不同，Android应用程序的结构要复杂得多，在大多数情况下，它们只有一个启动快捷方式的入口点，并且可以作为一个单一的整体进程运行。一个典型的Android应用程序是由多个应用程序组件构成的，包括活动，片段，服务，内容提供者和广播接收器。


大多数这些应用程序组件都是在Android操作系统使用的应用程序清单中声明的​​，清单决定如何将您的应用程序与其设备的整体用户体验的集成。如前所述，桌面应用程序传统上是以整体的方式运行，但正确的Android应用程序编写需要更加灵活，因为用户可以在设备上的不同应用程序不断切换流程和任务。


例如，请考虑在您最喜爱的社交网络应用程序中分享照片时会发生什么情况。该应用程序触发Android操作系统，启动相机应用程序来处理请求的相机意图。此时，用户离开了社交网络应用，但他们的体验是无缝的。相机应用程序又可能触发其他意图，例如启动文件选择器，该文件选择器可以启动另一个应用程序。最终用户回到社交网络应用程序并分享照片。此外，用户在这个过程的任何时候都可能被电话打断，并在打完电话后回来分享照片。


在Android中，这种应用程序跳转行为很常见，所以您的应用程序必须正确处理这些流程。请记住，移动设备是资源受限，所以在任何时候，操作系统可能需要杀死一些应用程序，以腾出空间给新的的应用或进程。所有这一切的关键是，您的应用程序组件可以单独和无序地启动，并可以在任何时候由用户或系统销毁。由于应用程序组件是短暂的，它们的生命周期（创建和销毁时）不在您的控制之下，因此您不应该在应用程序组件中存储任何应用程序数据或状态，并且应用程序组件不应相互依赖。


### 常见的架构原则

如果您不能使用应用程序组件来存储应用程序数据和状态，应该如何构建应用程序？

你应该关注的最重要的事情是在你的应用程序中，将所有代码写入Activity或Fragment是一个常见的错误。任何不处理UI或操作系统交互的代码都不应该在这些类中。尽可能保持精简可以避免许多生命周期相关的问题。记住，你不拥有这些类，它们只是体现操作系统和你的应用程序之间的契约的粘合剂。 Android操作系统可能会随时根据用户交互或其他因素（如低内存）来销毁它们。最好最大限度地减少对他们的依赖，以提供可靠的用户体验。

第二个重要的原则是你应该从一个模型驱动（数据）你的UI，最好是一个持久模型。持久性是理想的，原因有两个：如果操作系统破坏您的应用程序以释放资源，则您的用户不会丢失数据，即使网络连接不稳定或连接不上，您的应用程序也将继续工作。模型是负责处理应用程序数据的组件。它们独立于应用程序中的视图和应用程序组件，因此它们与这些组件的生命周期问题是隔离的。保持简单的UI代码和减少的应用程序逻辑，使管理更容易。将您的应用程序放在具有明确定义的管理数据的模型类上，使它们可测试，并能使您的应用程序保持一致。


### 推荐的应用架构

在本节中，我们将演示如何通过使用用例来构建使用体系结构组件的应用程序。

>注意：不可能有一种编写应用程序的方法，对每种情况都是最好的。也就是说，这个推荐的架构应该是大多数用例的一个很好的起点。如果您已经有了编写Android应用的好方法，则不需要更改。想象一下，我们正在构建一个显示用户配置文件的用户界面。该用户配置文件将使用REST API从我们自己的后端获取数据。

假如，我们正在构建一个显示用户信息的用户界面。该用户信息将使用REST API从我们自己的后端获取。


#### 构建用户界面
用户界面将由fragment  UserProfileFragment.java 和 布局文件user_profile_layout.xml组成。

为了驱动用户界面，我们的数据模型需要保存两个数据元素。

- User ID:用户的标识符。最好使用Fragment参数将此信息传递到Fragment中。如果Android操作系统破坏您的进程，这些信息将被保留，以便在您的应用下次重新启动时可用。

- User对象：持有用户数据的POJO

我们将创建一个基于ViewModel类的UserProfileViewModel来保存这些信息。

>ViewModel为特定的UI组件（如Fragment或Activity）提供数据，并处理与数据处理业务部分的通信，例如调用其他组件来加载数据或转发用户修改。 ViewModel不知道UI，并且不受配置更改的影响，例如由于旋转而重新创建Activity。

现在我们有3个文件:

- user_profile.xml

- UserProfileViewModel.java

- UserProfileFragment.java

下面是我们的开始的实现（布局文件为简单起见被省略）：

~~~
public class UserProfileViewModel extends ViewModel {
    private String userId;
    private User user;

    public void init(String userId) {
        this.userId = userId;
    }
    public User getUser() {
        return user;
    }
}
~~~

~~~
public class UserProfileFragment extends Fragment {
    private static final String UID_KEY = "uid";
    private UserProfileViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String userId = getArguments().getString(UID_KEY);
        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        viewModel.init(userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile, container, false);
    }
}
~~~

现在，我们有这三个代码模块，我们如何连接它们？毕竟，当ViewModel的用户字段被设置，我们需要一种方式来通知用户界面。这是使用LiveData类的地方。

---

LiveData是一个可观察的数据持有者。它允许应用程序中的组件观察LiveData对象的更改，而不会在它们之间创建明确的和严格的依赖关系路径。 LiveData还尊重您的应用程序组件（活动，片段，服务）的生命周期状态，并做正确的事情来防止对象泄漏，使您的应用程序不消耗更多的内存。

---

>注意：如果您已经在使用类似RxJava或Agera的库，则可以继续使用它们而不是LiveData。但是，当您使用它们或其他方法时，请确保正确处理生命周期，以便在相关的LifecycleOwner停止时停止数据流，并在销毁LifecycleOwner时销毁数据流。您还可以添加android.arch.lifecycle：reactivestreams工件以将LiveData与另一个反应流库（例如RxJava2）一起使用。

现在我们将UserProfileViewModel中的User字段替换为一个LiveData <User>，以便在数据更新时通知这个分段。 LiveData最棒的地方在于它具有生命周期感知能力，当不再需要引用时会自动清除引用。

~~~
public class UserProfileViewModel extends ViewModel {
    ...
    private User user;
    private LiveData<User> user;
    public LiveData<User> getUser() {
        return user;
    }
}
~~~

现在我们修改UserProfileFragment来观察数据并更新UI。

~~~
@Override
public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    viewModel.getUser().observe(this, user -> {
      // update UI
    });
}
~~~

每次更新用户数据时，都会调用onChanged回调，并刷新UI。

如果您熟悉使用可观察回调的其他库，您可能已经意识到，我们不必重写片段的onStop（）方法来停止观察数据。这对于LiveData来说并不是必须的，因为它是生命周期感知的，这意味着它不会调用回调，除非片段处于活动状态（收到onStart（），但没有收到onStop（））。当片段收到onDestroy（）时，LiveData也会自动移除观察者。

我们也没有做任何特殊的处理配置变化（例如，用户旋转屏幕）。当配置改变时，ViewModel会自动恢复，所以一旦新的片段生效，它将接收到同一个ViewModel的实例，回调将被立即调用当前数据。这就是ViewModel不能直接引用Views的原因。他们可以超越View的生命周期。请参阅ViewModel的生命周期。

#### 获取数据

现在我们已经将ViewModel连接到了片段，但是ViewModel如何获取用户数据呢？在这个例子中，我们假设我们的后端提供了一个REST API。我们将使用Retrofit库来访问我们的后端，尽管您可以自由使用不同的库来达到同样的目的。

这里是我们的改进的Web服务与我们的后端进行通信：

~~~
public interface Webservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("user") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("/users/{user}")
    Call<User> getUser(@Path("user") String userId);
}
~~~

ViewModel的一个原始的实现可以直接调用Web服务来获取数据并将其分配给用户对象。即使它可行，您的应用程序也将难以维持。它给ViewModel类提供了太多的责任，这违背了前面提到的分离原则。此外，ViewModel的范围与活动或片段生命周期相关联，因此，在生命周期结束时丢失所有数据是不好的用户体验。相反，我们的ViewModel将这个工作委托给一个新的Repository模块。

___

Repository模块负责处理数据操作。他们提供了一个干净的API到应用程序的其余部分。他们知道从何处获取数据以及在更新数据时调用哪些API。您可以将它们视为不同数据源（持久模型，Web服务，缓存等）之间的中介。

___

下面的UserRepository类使用WebService来获取用户数据项:

~~~
public class UserRepository {
    private Webservice webservice;
    // ...
    public LiveData<User> getUser(int userId) {
        // This is not an optimal implementation, we'll fix it below
        final MutableLiveData<User> data = new MutableLiveData<>();
        webservice.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // error case is left out for brevity
                data.setValue(response.body());
            }
        });
        return data;
    }
}
~~~

即使Repository模块看起来没有必要，它也有着重要的作用。它从应用程序的其余部分提取数据源。现在我们的ViewModel不知道数据是由Webservice获取的，这意味着我们可以根据需要将其交换为其他实现。

>注意：为简单起见，我们忽略了网络错误的情况。有关公开错误和加载状态的替代实现，请参阅附录：公开网络状态。

#### 管理组件之间的依赖关系：

上面的UserRepository类需要Webservice的一个实例来完成它的工作。它可以简单地创建它，但要做到这一点，它也需要知道Webservice类的依赖关系来构造它。这会使代码复杂化和重复（例如，每个需要Webservice实例的类都需要知道如何用它的依赖关系来构造它）。此外，UserRepository可能不是唯一需要Web服务的类。如果每个类创建一个新的WebService，这将是非常资源沉重。

有两种模式可以用来解决这个问题：

- 依赖注入：依赖注入允许类在不构造它们的情况下定义它们的依赖关系。在运行时，另一个类负责提供这些依赖关系。我们推荐Google的Dagger 2库在Android应用程序中实现依赖注入。 Dagger 2通过遍历依赖关系树来自动构造对象，并为依赖关系提供编译时间保证。

- 服务定位器：服务定位器提供了一个注册表，类可以获得它们的依赖而不是构建它们。实现起来比依赖注入（DI）更容易，所以如果你不熟悉DI，可以使用Service Locator。

这些模式允许您扩展您的代码，因为它们提供了用于管理依赖关系的清晰模式，无需重复代码或增加复杂性。他们两人也允许交换实现测试;这是使用它们的主要好处之一

在这个例子中，我们将使用Dagger 2来管理依赖关系。

#### 连接ViewModel和存储库

现在我们修改我们的UserProfileViewModel来使用仓库。

~~~
public class UserProfileViewModel extends ViewModel {
    private LiveData<User> user;
    private UserRepository userRepo;

    @Inject // UserRepository parameter is provided by Dagger 2
    public UserProfileViewModel(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void init(String userId) {
        if (this.user != null) {
            // ViewModel is created per Fragment so
            // we know the userId won't change
            return;
        }
        user = userRepo.getUser(userId);
    }

    public LiveData<User> getUser() {
        return this.user;
    }
}
~~~

###  缓存数据

上面的存储库实现对抽象调用Web服务是有好处的，但是因为它只依赖于一个数据源，所以它不是很有用。

上面的UserRepository实现的问题是，在获取数据之后，它不保留在任何地方。如果用户离开UserProfileFragment并返回到该应用程序，则应用程序会重新获取数据。这是不好的，原因有两个：浪费宝贵的网络带宽并强制用户等待新的查询完成。为了解决这个问题，我们将添加一个新的数据源到我们的UserRepository中，它将把用户对象缓存在内存中。


~~~
@Singleton  // informs Dagger that this class should be constructed once
public class UserRepository {
    private Webservice webservice;
    // simple in memory cache, details omitted for brevity
    private UserCache userCache;
    public LiveData<User> getUser(String userId) {
        LiveData<User> cached = userCache.get(userId);
        if (cached != null) {
            return cached;
        }

        final MutableLiveData<User> data = new MutableLiveData<>();
        userCache.put(userId, data);
        // this is still suboptimal but better than before.
        // a complete implementation must also handle the error cases.
        webservice.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                data.setValue(response.body());
            }
        });
        return data;
    }
}
~~~

#### 持久化数据

在我们当前的实现中，如果用户旋转屏幕或离开并返回到应用程序，则现有UI将立即可见，因为存储库从内存中缓存中检索数据。但是，如果用户离开应用程序，并在Android操作系统杀死该进程后数小时后回来，会发生什么？

在目前的实施中，我们将需要从网络上重新获取数据。这不仅是一个糟糕的用户体验，而且会浪费，因为它会使用移动数据重新获取相同的数据。您可以简单地通过缓存Web请求来解决这个问题，但是会产生新的问题。如果相同的用户数据显示来自另一种类型的请求（例如，获取朋友列表），会发生什么情况？那么你的应用程序可能会显示不一致的数据，这是一个混乱的用户体验充其量。例如，由于好友列表请求和用户请求可以在不同的时间执行，所以相同用户的数据可能会以不同的方式显示。您的应用需要合并它们以避免显示不一致的数据。

处理这个问题的正确方法是使用持久模型。这是Room持久性库优点的地方。
>Room是一个对象映射库，提供本地数据持久性和最小的样板代码。在编译时，它会根据模式验证每个查询，以便断开的SQL查询导致编译时错误，而不是运行时失败。Room抽象出一些使用原始SQL表和查询的底层实现细节。它还允许观察对数据库数据（包括集合和连接查询）的更改，通过LiveData对象公开这些更改。另外，它明确定义了解决常见问题的线程约束，例如访问主线程上的存储。



>注意：如果您的应用程序已经使用另一个持久性解决方案（如SQLite对象关系映射（ORM）），则不需要使用Room替换现有的解决方案。但是，如果您正在编写新的应用程序或重构现有应用程序，我们建议使用Room来保存应用程序的数据。这样，您可以利用库的抽象和查询验证功能。

要使用Room，我们需要定义我们的本地模式。首先，使用@Entity注释User类，将其标记为数据库中的表。

~~~
@Entity
class User {
  @PrimaryKey
  private int id;
  private String name;
  private String lastName;
  // getters and setters for fields
}
~~~

然后，通过为您的应用程序扩展RoomDatabase来创建一个数据库类：

~~~
@Database(entities = {User.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
}
~~~

注意MyDatabase是抽象的。Room自动提供一个实施。有关详细信息，请参见Room文档

现在我们需要一种将用户数据插入数据库的方法。为此，我们将创建一个数据访问对象（DAO）。

~~~
@Dao
public interface UserDao {
    @Insert(onConflict = REPLACE)
    void save(User user);
    @Query("SELECT * FROM user WHERE id = :userId")
    LiveData<User> load(String userId);
}
~~~


然后，从我们的数据库类中引用DAO。

~~~
@Database(entities = {User.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
~~~

注意load方法返回一个LiveData <User>。Room知道数据库何时被修改，当数据改变时它会自动通知所有活动的观察者。因为它使用的是LiveData，所以这将是有效的，因为它只会在至少有一个活动观察者的情况下更新数据。

>注意：Room根据表格修改检查失效，这意味着它可能发送误报通知。

现在我们可以修改我们的UserRepository以合并Room数据源。

~~~
@Singleton
public class UserRepository {
    private final Webservice webservice;
    private final UserDao userDao;
    private final Executor executor;

    @Inject
    public UserRepository(Webservice webservice, UserDao userDao, Executor executor) {
        this.webservice = webservice;
        this.userDao = userDao;
        this.executor = executor;
    }

    public LiveData<User> getUser(String userId) {
        refreshUser(userId);
        // return a LiveData directly from the database.
        return userDao.load(userId);
    }

    private void refreshUser(final String userId) {
        executor.execute(() -> {
            // running in a background thread
            // check if user was fetched recently
            boolean userExists = userDao.hasUser(FRESH_TIMEOUT);
            if (!userExists) {
                // refresh the data
                Response response = webservice.getUser(userId).execute();
                // TODO check for error etc.
                // Update the database.The LiveData will automatically refresh so
                // we don't need to do anything else here besides updating the database
                userDao.save(response.body());
            }
        });
    }
}
~~~

请注意，即使我们更改了UserRepository中数据的来源，我们也不需要更改UserProfileViewModel或UserProfileFragment。这是抽象提供的灵活性。这对于测试也很好，因为在测试UserProfileViewModel的时候可以提供一个伪造的UserRepository。


现在我们的代码是完整的。如果用户以后回到相同的用户界面，他们会立即看到用户信息，因为我们持久化了。同时，如果数据陈旧，我们的仓库将在后台更新数据。当然，根据您的使用情况，如果数据太旧，您可能不希望显示持久数据。


在一些使用情况下，如下拉刷新，UI显示用户是否正在进行网络操作是非常重要的。将UI操作与实际数据分开是一种很好的做法，因为它可能因各种原因而更新（例如，如果我们获取朋友列表，则可能会再次触发同一用户，触发LiveData <User>更新）。从用户界面的角度来看，一个正在进行中请求只是另一个数据点，类似于其他任何数据（如用户对象）。

这个用例有两个常见的解决方案：

- 更改getUser以返回包含网络操作状态的LiveData。附录中提供了一个示例实现：公开网络状态部分。

- 在存储库类中提供另一个可以返回用户刷新状态的公共函数。如果只想响应显式的用户操作（如下拉刷新）来显示网络状态，则此选项更好。

#### 单一的事实来源

不同的REST API端点通常返回相同的数据。例如，如果我们的后端拥有另一个返回朋友列表的端点，则同一个用户对象可能来自两个不同的API端点，也许端点不同。如果UserRepository原样返回来自Webservice请求的响应，那么我们的UI可能会显示不一致的数据，因为这些请求之间的数据可能在服务器端发生更改。这就是为什么在UserRepository实现中，Web服务回调只是将数据保存到数据库中。然后，对数据库的更改将触发活动LiveData对象上的回调。

在这个模型中，数据库充当真实数据的单一来源，应用程序的其他部分通过存储库访问它。即使您使用磁盘缓存，都建议您将存储库数据源指定为应用程序其余部分的单一来源。

#### 测试

我们已经提到分离的好处之一就是可测试性。让我们看看我们如何测试每个代码模块。

- 用户界面：这将是您唯一需要Android UI Instrumentation测试的时间。测试UI代码的最好方法是创建一个Espresso测试。您可以创建片段并为其提供一个模拟的ViewModel。由于该片段只与ViewModel交谈，因此模拟它将足以完全测试此UI。

- ViewModel: ViewModel可以使用JUnit测试进行测试。你只需要模拟UserRepository来测试它。

- UserRepository:您也可以使用JUnit测试来测试UserRepository。你需要模拟Web服务和DAO。您可以测试它是否进行正确的Web服务调用，将结果保存到数据库中，如果数据已缓存且最新，则不会发出任何不必要的请求。既然Webservice和UserDao都是接口，你可以模拟它们或者为更复杂的测试用例创建假实现。

- UserDao: 测试DAO类的推荐方法是使用仪器测试。由于这些测试不需要任何用户界面，他们仍然会运行得很快。对于每个测试，您可以创建一个内存数据库，以确保测试没有任何副作用（如更改磁盘上的数据库文件）。Room还允许指定数据库实现，以便通过提供SupportSQLiteOpenHelper的JUnit实现来测试它。通常不建议使用这种方法，因为设备上运行的SQLite版本可能与主机上的SQLite版本不同。

- Webservice:使测试独立于外界是很重要的，所以即使是Web服务测试也应该避免对后端进行网络调用。有很多library可以帮助你。例如，MockWebServer是一个伟大的库，可以帮助您为测试创建一个假的本地服务器。

- Testing Artifacts: 架构组件提供了一个maven工件来控制其后台线程。在android.arch.core：核心测试工件内部，有2个JUnit规则：
    - InstantTaskExecutorRule：此规则可用于强制架构组件立即在调用线程上执行任何后台操作。
    - CountingTaskExecutorRule：此规则可用于检测测试，以等待体系结构组件的后台操作或将其作为闲置资源连接到Espresso。

### 架构图

下图显示了我们推荐的体系结构中的所有模块以及它们如何相互交互：


### 指导原则

编程是一个创造性的领域，构建Android应用程序也不是一个例外。解决问题的方法有很多种，可以在多个活动或片段之间传递数据，检索远程数据并将其保存在本地以进行脱机模式，也可以使用许多其他常见应用程序遇到的情况。

尽管以下建议不是强制性的，但是我们的经验是，遵循这些建议将使您的代码基础更加健壮，可测试和可维护。

- 您在清单中定义的入口点（活动，服务，广播接收器等）不是数据的来源。相反，他们只应该协调与该入口点相关的数据子集。由于每个应用程序组件的寿命相当短，这取决于用户与设备的交互以及运行时的整体当前运行状况，因此您不希望这些入口点中的任何一个成为数据源。

- 无情地在应用程序的各个模块之间创建明确界定的责任。例如，不要将从网络加载数据的代码跨代码库中的多个类或包传播。同样，不要把不相关的职责 - 比如数据缓存和数据绑定 - 放到同一个类中。

- 尽可能少地从每个模块公开。不要试图创建“only one”的快捷方式，从一个模块公开内部实现细节。您可能在短期内获得一些时间，但随着您的代码库的发展，您将多次支付技术债务。

- 在定义模块之间的交互时，请考虑如何使每个模块独立地进行测试。例如，如果有一个定义良好的API从网络中获取数据，将会更容易测试将数据保存在本地数据库中的模块。相反，如果将这两个模块的逻辑混合在一起，或者在整个代码库中撒上网络代码，那么要测试就更加困难了。

- 你的应用程序的核心是什么让它从其他中脱颖而出。不要花费时间重复发明轮子，或者一次又一次地写出相同的样板代码。相反，将精力集中在让您的应用独一无二的东西上，让Android架构组件和其他推荐的库处理重复的样板。

-  你的应用程序的核心是什么让它从其他中脱颖而出。不要花费时间重复发明轮子，或者一次又一次地写出相同的样板代码。相反，将精力集中在让您的应用独一无二的东西上，让Android架构组件和其他推荐的库处理重复的样板。

- 坚持尽可能多的相关和新鲜的数据，以便您的应用程序在设备处于离线模式时可用。虽然您可以享受持续高速的连接，但用户可能不会。

- 您的存储库应该指定一个数据源作为单一的事实来源。无论何时您的应用程序需要访问这些数据，都应始终从单一的事实源头开始。有关更多信息，请参阅单一来源的真相。

###附录：网络状态

在上面推荐的应用程序体系结构部分，我们故意省略网络错误和加载状态，以保持样本简单。在本节中，我们将演示如何使用Resource类公开网络状态来封装数据及其状态。

以下是一个示例实现：
~~~
//a generic class that describes a data with a status
public class Resource<T> {
    @NonNull public final Status status;
    @Nullable public final T data;
    @Nullable public final String message;
    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }
}
~~~

因为从磁盘加载数据的时候是一个常见的用例，所以我们要创建一个可以在多个地方重复使用的帮助类NetworkBoundResource。以下是NetworkBoundResource的决策树：

它通过观察资源的数据库开始。当第一次从数据库加载条目时，NetworkBoundResource检查结果是否足够好以便分派和/或从网络中获取。请注意，这两种情况可能同时发生，因为您可能希望在从网络更新缓存数据时显示缓存的数据。

如果网络呼叫成功完成，则将响应保存到数据库中并重新初始化流。如果网络请求失败，我们直接发送失败。

>注意：在将新数据保存到磁盘之后，我们会重新初始化数据库中的数据流，但通常我们不需要这样做，因为数据库将分派更改。另一方面，依靠数据库来调度变化将依赖于不好的副作用，因为如果数据没有变化，数据库可以避免调度变化，那么它可能会中断。我们也不希望发送从网络到达的结果，因为这将违背单一的事实来源（也许在数据库中有触发器会改变保存的值）。如果没有新的数据，我们也不想派遣SUCCESS，因为它会向客户发送错误的信息。

以下是NetworkBoundResource类为其子项提供的公共API：

~~~
// ResultType: Type for the Resource data
// RequestType: Type for the API response
public abstract class NetworkBoundResource<ResultType, RequestType> {
    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    // Called with the data in the database to decide whether it should be
    // fetched from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Called to get the cached data from the database
    @NonNull @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // Called to create the API call.
    @NonNull @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected void onFetchFailed() {
    }

    // returns a LiveData that represents the resource, implemented
    // in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData();
}
~~~

请注意，上面的类定义了两个类型参数（ResultType，RequestType），因为从API返回的数据类型可能与本地使用的数据类型不匹配。


另外请注意，上面的代码使用ApiResponse进行网络请求。 ApiResponse是Retrofit2.Call类的一个简单的包装，将其响应转换为LiveData。

以下是NetworkBoundResource类的其余实现：

~~~
public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    NetworkBoundResource() {
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource,
                        newData -> result.setValue(Resource.success(newData)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        // we re-attach dbSource as a new source,
        // it will dispatch its latest value quickly
        result.addSource(dbSource,
                newData -> result.setValue(Resource.loading(newData)));
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                saveResultAndReInit(response);
            } else {
                onFetchFailed();
                result.addSource(dbSource,
                        newData -> result.setValue(
                                Resource.error(response.errorMessage, newData)));
            }
        });
    }

    @MainThread
    private void saveResultAndReInit(ApiResponse<RequestType> response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                saveCallResult(response.body);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // we specially request a new live data,
                // otherwise we will get immediately last cached value,
                // which may not be updated with latest results received from network.
                result.addSource(loadFromDb(),
                        newData -> result.setValue(Resource.success(newData)));
            }
        }.execute();
    }

    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}
~~~

现在，我们可以使用NetworkBoundResource将我们的磁盘和网络绑定的User实现写入到存储库中。

~~~
class UserRepository {
    Webservice webservice;
    UserDao userDao;

    public LiveData<Resource<User>> loadUser(final String userId) {
        return new NetworkBoundResource<User,User>() {
            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return rateLimiter.canFetch(userId) && (data == null || !isFresh(data));
            }

            @NonNull @Override
            protected LiveData<User> loadFromDb() {
                return userDao.load(userId);
            }

            @NonNull @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return webservice.getUser(userId);
            }
        }.getAsLiveData();
    }
}

~~~

