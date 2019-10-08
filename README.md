# mock-solution branch
This branch shows what the testing might look like if we wanted 
to mock out the GuitarService and the GuitarRepository.  Note that this
is just for demo purposes.  

Major changes are...
* GuitarServiceTests - Mocking GuitarRepository
  * @RunWith(MockitoJUnitRunner.class)
  * @Mock GuitarRepository.
  * Change GuitarService to use constructor injection for repo
  * Remove direct calls to repository for setup
  * Use when/thenReturn to setup the behavior of GuitarRepository
* GuitarControllerTests - Mocking GuitarService
  * @RunWith(SpringRunner.class)
  * @WebMvcTest(GuitarController.class)
  * @MockBean GuitarService (still Mockito)
  * Remove all references to GuitarRepository
  * Use when/thenReturn to setup the behavior of GuitarService
  
If you want to look at the diff in IntelliJ, From the context menu of the file,
(right click), chose `git / compare with branch` then click on `master`.  Also
available from the top level menu in `VCS`

# guitars-api
This is a simple implementation of a rest api microservice.  
It is intended to give a quick example of what a rest api
might look like implemented.  Please use for reference only, knowing
that a production application would most likely be very different.

Please, feel free to fork/clone this for your own learning / teaching 
purposes.  I would appreciate a shout out if you share it, but it 
is not required.  Comments or questions are also welcome as well.

Thanks,

Rob