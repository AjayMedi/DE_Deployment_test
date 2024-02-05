import unittest

class TestDummyUnitTest(unittest.TestCase):

    def test_health_check_when_no_error_log_directory(self):
    
        self.assertTrue("True" == "True", 'Dummy Unit Test')


if __name__ == '__main__':
    unittest.main()
