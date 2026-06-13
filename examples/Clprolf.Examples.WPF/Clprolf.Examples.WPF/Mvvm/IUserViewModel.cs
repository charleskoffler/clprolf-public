using Clprolf.ArchUnitNet.Attributes;
using System.ComponentModel;

namespace Clprolf.Examples.WPF.Mvvm
{
    [ClAgent]
    [ClFamily]
    public interface IUserViewModel: INotifyPropertyChanged
    {
        void ExecuteAdd(object? parameter);

        void ExecuteUpdate(object? parameter);

        void ExecuteDelete(object? parameter);

        public bool CanExecuteAdd(object? parameter);

        public bool CanExecuteSelection(object? parameter);
    }
}