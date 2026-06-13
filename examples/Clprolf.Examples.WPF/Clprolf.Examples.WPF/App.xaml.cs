using Clprolf.ArchUnitNet.Attributes;
using Clprolf.Examples.WPF.Agents.Impl.SystemOriented;
using Clprolf.Examples.WPF.Mvvm.Impl;
using System.Configuration;
using System.Data;
using System.Windows;

namespace Clprolf.Examples.WPF
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    [ClWorker]
    public partial class App : Application
    {
        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);

            var vm = new UserViewModel();
            var mainWin = new UserWindow(vm);

            mainWin.Show();
        }
    }

}
