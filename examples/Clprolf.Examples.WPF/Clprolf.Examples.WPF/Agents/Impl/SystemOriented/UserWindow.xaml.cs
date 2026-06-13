using Clprolf.ArchUnitNet.Attributes;
using Clprolf.Examples.WPF.Mvvm.Impl;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Clprolf.Examples.WPF.Agents.Impl.SystemOriented
{
    /// <summary>
    /// Interaction logic for UserWindow.xaml
    /// </summary>
    ///
    
    [ClAgent]
    public partial class UserWindow : Window
    {
        public UserWindow(UserViewModel vm)
        {
            InitializeComponent();
            DataContext = vm;
        }
    }
}